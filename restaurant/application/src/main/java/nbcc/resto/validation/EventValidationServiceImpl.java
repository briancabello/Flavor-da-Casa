package nbcc.resto.validation;

import nbcc.common.service.AnnotationValidationService;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.EventDto;
import org.springframework.stereotype.Service;
import java.util.Collection;


@Service
public class EventValidationServiceImpl implements EventValidationService {

    private final AnnotationValidationService annotationValidationService;

    public EventValidationServiceImpl(AnnotationValidationService annotationValidationService) {
        this.annotationValidationService = annotationValidationService;
    }

    @Override
    public Collection<ValidationError> validate(EventDto eventDto) {
        Collection<ValidationError> errors = annotationValidationService.validate(eventDto);

        if (eventDto.getStartDate() != null && eventDto.getEndDate() != null){
            if(eventDto.getEndDate().isBefore(eventDto.getStartDate())){
                errors.add(new ValidationError("End date cannot be before start date", "endDate"));
            }
        }
        return errors;
    }
}
