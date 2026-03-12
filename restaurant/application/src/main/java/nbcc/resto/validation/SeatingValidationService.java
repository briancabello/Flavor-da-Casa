package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Seating;
import nbcc.resto.repository.SeatingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class SeatingValidationService {

    private final AnnotationValidationService annotationValidationService;
    private final SeatingRepository seatingRepository;

    public SeatingValidationService(
            AnnotationValidationService annotationValidationService,
            SeatingRepository seatingRepository
    ) {
        this.annotationValidationService = annotationValidationService;
        this.seatingRepository = seatingRepository;
    }

    public Collection<ValidationError> validate(Seating seating) {
        var errors = new ArrayList<ValidationError>();
        errors.addAll(annotationValidationService.validate(seating));
        errors.addAll(validateTableSelection(seating));

        // Only check overlap if no basic validation errors
        if (errors.isEmpty()) {
            errors.addAll(validateTableOverlap(seating));
        }

        return errors;
    }

    private Collection<ValidationError> validateTableSelection(Seating seating) {
        if (seating.getSelectedTableIds() == null || seating.getSelectedTableIds().isEmpty()) {
            return List.of(new ValidationError("At least one table must be selected", "selectedTableIds"));
        }
        return List.of();
    }

    private Collection<ValidationError> validateTableOverlap(Seating seating) {
        var newStart = seating.getStartDateTime();
        var newEnd = seating.getEndDateTime();

        for (Long tableId : seating.getSelectedTableIds()) {
            var existingSeatings = seatingRepository.getSeatingsByTableId(tableId);

            for (Seating existing : existingSeatings) {
                if (seating.getId() != null && seating.getId().equals(existing.getId())) continue;

                var exStart = existing.getStartDateTime();
                var exEnd = existing.getEndDateTime();

                if (newStart.isBefore(exEnd) && newEnd.isAfter(exStart)) {
                    return List.of(new ValidationError(
                            "One or more selected tables are already in use during this time period.",
                            "selectedTableIds"));
                }
            }
        }
        return List.of();
    }
}