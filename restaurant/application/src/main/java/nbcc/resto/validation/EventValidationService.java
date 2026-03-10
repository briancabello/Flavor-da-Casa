package nbcc.resto.validation;

import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.EventDto;

import java.util.Collection;


public interface EventValidationService {
    Collection<ValidationError> validate(EventDto eventDto);
}
