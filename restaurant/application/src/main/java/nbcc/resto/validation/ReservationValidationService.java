package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.ReservationDto;
import nbcc.resto.repository.EventRepository;
import nbcc.resto.repository.SeatingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ReservationValidationService {

    private final AnnotationValidationService annotationValidationService;
    private final EventRepository eventRepository;
    private final SeatingRepository seatingRepository;

    public ReservationValidationService(
            AnnotationValidationService annotationValidationService,
            EventRepository eventRepository,
            SeatingRepository seatingRepository
    ) {
        this.annotationValidationService = annotationValidationService;
        this.eventRepository = eventRepository;
        this.seatingRepository = seatingRepository;
    }

    public Collection<ValidationError> validate(ReservationDto reservation) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(reservation));

        if (errors.isEmpty()) {
            errors.addAll(validateEventExists(reservation));
            errors.addAll(validateSeatingExists(reservation));
        }

        return errors;
    }

    private Collection<ValidationError> validateEventExists(ReservationDto reservation) {
        if (reservation.getEventId() == null) {
            return List.of();
        }
        var event = eventRepository.get(reservation.getEventId());
        if (event.isEmpty()) {
            return List.of(new ValidationError("Selected event does not exist", "eventId"));
        }
        return List.of();
    }

    private Collection<ValidationError> validateSeatingExists(ReservationDto reservation) {
        if (reservation.getSeatingId() == null) {
            return List.of();
        }
        var seating = seatingRepository.get(reservation.getSeatingId());
        if (seating.isEmpty()) {
            return List.of(new ValidationError("Selected seating does not exist", "seatingId"));
        }
        if (reservation.getEventId() != null &&
                !seating.get().getEventId().equals(reservation.getEventId())) {
            return List.of(new ValidationError("Selected seating does not belong to the selected event", "seatingId"));
        }
        return List.of();
    }
}
