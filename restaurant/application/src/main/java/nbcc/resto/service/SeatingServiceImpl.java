package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.DiningTableRepository;
import nbcc.resto.repository.EventRepository;
import nbcc.resto.repository.SeatingRepository;
import nbcc.resto.validation.SeatingValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class SeatingServiceImpl implements SeatingService {

    private final Logger logger = LoggerFactory.getLogger(SeatingServiceImpl.class);

    private final SeatingRepository seatingRepository;
    private final DiningTableRepository diningTableRepository;
    private final EventRepository eventRepository;
    private final SeatingValidationService validationService;

    public SeatingServiceImpl(SeatingRepository seatingRepository, DiningTableRepository diningTableRepository, EventRepository eventRepository, SeatingValidationService validationService) {
        this.seatingRepository = seatingRepository;
        this.diningTableRepository = diningTableRepository;
        this.eventRepository = eventRepository;
        this.validationService = validationService;
    }

    @Override
    public Result<Collection<Seating>> getAll() {
        return null;
    }

    @Override
    public Result<Collection<Seating>> getByEvent(Long eventId) {
        return null;
    }

    @Override
    public Result<Collection<Seating>> getSeatingsByTableId(Long tableId) {
        return null;
    }

    @Override
    public ValidatedResult<Seating> create(Seating seating) {
        try {
            if (seating.getEventId() != null) {
                var eventOpt = eventRepository.get(seating.getEventId());

                // Apply default duration if user left it blank
                if (eventOpt.isPresent() && seating.getDuration() == null) {
                    seating.setDuration(eventOpt.get().getDuration());
                }
            }

            var errors = validationService.validate(seating);

            if (errors.isEmpty()) {
                return ValidationResults.success(seatingRepository.create(seating));
            } else {
                logger.debug("Validation errors for seating create {}: {}", seating, errors);
                return ValidationResults.invalid(seating, errors);
            }
        } catch (Exception e) {
            logger.error("Error creating seating {}", seating, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try {
            seatingRepository.delete(id);
            logger.debug("Seating with id {} deleted", id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting seating with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }
}
