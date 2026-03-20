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
    private final ReservationService reservationService;

    public SeatingServiceImpl(SeatingRepository seatingRepository, DiningTableRepository diningTableRepository, EventRepository eventRepository, SeatingValidationService validationService, ReservationService reservationService) {
        this.seatingRepository = seatingRepository;
        this.diningTableRepository = diningTableRepository;
        this.eventRepository = eventRepository;
        this.validationService = validationService;
        this.reservationService = reservationService;
    }

    @Override
    public Result<Collection<Seating>> getAll() {
        try {
            return ValidationResults.success(seatingRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all seatings", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<Seating>> getByEvent(Long eventId) {
        try {
            return ValidationResults.success(seatingRepository.getByEvent(eventId));
        } catch (Exception e) {
            logger.error("Error retrieving seatings for event with id: {}", eventId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<Seating>> getSeatingsByTableId(Long tableId) {
        try {
            return ValidationResults.success(seatingRepository.getSeatingsByTableId(tableId));
        } catch (Exception e) {
            logger.error("Error retrieving seatings for table with id: {}", tableId, e);
            return ValidationResults.error(e);
        }
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
    public Result<Seating> get(Long id) {
        try {
            var existingOpt = seatingRepository.get(id);

            if (existingOpt.isEmpty()) {
                return ValidationResults.invalid(null, "Seating not found", "id");
            }

            return ValidationResults.success(existingOpt.get());

        } catch (Exception e) {
            logger.error("Error retrieving seating with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Seating> update(Seating seating) {
        try {

            if (seating.getId() == null) {
                return ValidationResults.invalid(null, "Cannot update: Seating ID is missing", "id");
            }


            var existingOpt = seatingRepository.get(seating.getId());
            if (existingOpt.isEmpty()) {
                return ValidationResults.invalid(null, "Seating not found", "id");
            }

            Seating existingSeating = existingOpt.get();


            // force the updated DTO to keep the original status and created date.
            seating.setStatus(existingSeating.isStatus());
            seating.setCreatedDate(existingSeating.getCreatedDate());


            var errors = validationService.validate(seating);

            if (!errors.isEmpty()) {
                 logger.debug("Validation errors for seating update: {}", errors);
                return ValidationResults.invalid(seating, errors);
            }

            Seating updatedSeating = seatingRepository.update(seating);
            return ValidationResults.success(updatedSeating);

        } catch (Exception e) {
            logger.error("Error updating seating", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Seating> delete(Long id) {
        try {

            var existingOpt = seatingRepository.get(id);
            if (existingOpt.isEmpty()) {
                return ValidationResults.invalid(null, "Seating not found", "id");
            }
            Seating seating = existingOpt.get();


            boolean hasReservations = reservationService.existsBySeatingId(id);


            if (hasReservations) {
                seating.setStatus(false);


                seatingRepository.update(seating);
                return ValidationResults.success(seating);
            } else {

                seatingRepository.delete(id);
                return ValidationResults.success(seating);
            }

        } catch (Exception e) {
            logger.error("Error deleting seating", e);
            return ValidationResults.error(e);
        }
    }


}
