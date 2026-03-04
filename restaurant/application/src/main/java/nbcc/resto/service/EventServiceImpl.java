package nbcc.resto.service;


import nbcc.common.result.Result;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Event;
import nbcc.resto.repository.EventRepository;
import nbcc.resto.validation.EventValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public Result<Collection<Event>> getAll() {
        try {
            return ValidationResults.success(eventRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all events", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Event> get(Long id) {
        try {
            Optional<Event> event = eventRepository.get(id);
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
    public Result<Event> create(Event event) {

        try {
            var errors = validationService.validate(event);

            if(eventRepository.exists(event.getName())){
                errors.add(new ValidationError("name", "Event with this name already exists"));
            }
            if(!errors.isEmpty()){
                logger.debug("Validation errors for event create: {}", errors);
                return ValidationResults.invalid(event, errors);
            }
            return ValidationResults.success(eventRepository.create(event));
        } catch (Exception e) {
            logger.error("Error creating event", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Event> update(Event event) {
        try {
            var errors = validationService.validate(event);

            if(event.getId() == null || eventRepository.get(event.getId()).isEmpty()){
                errors.add(new ValidationError("id", "Cannot update: Event does not exist", event.getId()));
            }
            if (!errors.isEmpty()) {
                logger.debug("Validation errors for event update: {}", errors);
                return ValidationResults.invalid(event, errors);
            }
            return ValidationResults.success(eventRepository.update(event));

        } catch (Exception e) {
            logger.error("Error updating event", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Event> delete(Long id) {
        try {
            var existing = eventRepository.get(id);

            if (existing.isEmpty()) {
                return ValidationResults.invalid(null, "Event not found", "id");
            }

            eventRepository.delete(id);
            return ValidationResults.success(existing.get());
        } catch (Exception e) {
            logger.error("Error deleting event with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<Event>> search(String query, LocalDateTime start, LocalDateTime end) {
        try {
            return ValidationResults.success(eventRepository.search(query, start, end));
        } catch (Exception e) {
            logger.error("Error searching events", e);
            return ValidationResults.error(e);
        }
    }
}
