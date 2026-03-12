package nbcc.resto.service;


import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.EventDto;
import nbcc.resto.repository.EventRepository;
import nbcc.resto.validation.EventValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    private final EventRepository eventRepository;
    private final EventValidationService validationService;

    public EventServiceImpl(EventRepository eventRepository, EventValidationService validationService) {
        this.eventRepository = eventRepository;
        this.validationService = validationService;
    }


    @Override
    public Result<Collection<EventDto>> getAll() {
        try {
            return ValidationResults.success(eventRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all events", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<EventDto> get(Long id) {
        try {
            Optional<EventDto> event = eventRepository.get(id);
            if(event.isEmpty()){
                return ValidationResults.invalid(null, "Event not found", "id");
            }
            return ValidationResults.success(event.get());
        } catch (Exception e) {
            logger.error("Error retrieving event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<EventDto> create(EventDto eventDto) {

        try {
            var errors = validationService.validate(eventDto);

            if(eventRepository.exists(eventDto.getName())){
                errors.add(new ValidationError("Event with this name already exists", "name", eventDto.getName()));
            }
            if(!errors.isEmpty()){
                logger.debug("Validation errors for event create: {}", errors);
                return ValidationResults.invalid(eventDto, errors);
            }
            return ValidationResults.success(eventRepository.create(eventDto));
        } catch (Exception e) {
            logger.error("Error creating event", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<EventDto> update(EventDto eventDto) {
        try {

            if (eventDto.getId() == null) {
                return ValidationResults.invalid(null, "Cannot update: Event ID is missing", "id");
            }

            var existingOpt = eventRepository.get(eventDto.getId());
            if (existingOpt.isEmpty()) {
                return ValidationResults.invalid(null, "Event not found", "id");
            }

            EventDto existingEvent = existingOpt.get();
            var errors = validationService.validate(eventDto);

            boolean nameChanged = !existingEvent.getName().equalsIgnoreCase(eventDto.getName());
            if (nameChanged && eventRepository.exists(eventDto.getName())) {
                errors.add(new ValidationError("An event with this name already exists", "name", eventDto.getName()));
            }

            if (!errors.isEmpty()) {
                logger.debug("Validation errors for event update: {}", errors);
                return ValidationResults.invalid(eventDto, errors);
            }

            return ValidationResults.success(eventRepository.update(eventDto));

        } catch (Exception e) {
            logger.error("Error updating event", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<EventDto> delete(Long id) {
        try {
            var existingOpt  = eventRepository.get(id);

            if (existingOpt.isEmpty()) {
                return ValidationResults.invalid(null, "Event not found", "id");
            }

            EventDto eventDto = existingOpt.get();

            
            if (eventDto.getEndDate() != null && eventDto.getEndDate().isBefore(LocalDate.now())) {
                
                eventDto.setArchived(true);
                eventDto.setActive(false);

                eventRepository.update(eventDto);
                logger.debug("Event with id {} archived (past event)", id);

                return ValidationResults.success(eventDto);

            } else {
                eventRepository.delete(id);
                logger.debug("Event with id {} deleted", id);
            }

            return ValidationResults.success(eventDto);
        } catch (Exception e) {
            logger.error("Error deleting event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<Collection<EventDto>> search(String query, LocalDate startDate, LocalDate endDate) {
        try {

            LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
            LocalDateTime end = (endDate != null) ? endDate.atTime(23, 59, 59) : null;

            return ValidationResults.success(eventRepository.search(query, start, end));
        } catch (Exception e) {
            logger.error("Error searching events", e);
            return ValidationResults.error(e);
        }
    }
}
