package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Event;
import org.springframework.stereotype.Service;
import java.util.Collection;


@Service
public class EventValidationServiceImpl implements EventValidationService {

    private final AnnotationValidationService annotationValidationService;

    public EventValidationServiceImpl(AnnotationValidationService annotationValidationService) {
        this.annotationValidationService = annotationValidationService;
    }

    @Override
    public Collection<ValidationError> validate(Event event) {
        Collection<ValidationError> errors = annotationValidationService.validate(event);

        if (event.getStartDate() != null && event.getEndDate() != null){
            if(event.getEndDate().isBefore(event.getStartDate())){
                errors.add(new ValidationError("End date cannot be before start date", "endDate"));
            }
        }
        return errors;
    }
}
