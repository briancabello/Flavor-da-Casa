package nbcc.resto.validation;

import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.Event;
import org.springframework.stereotype.Service;

import java.util.Collection;


public interface EventValidationService {
    Collection<ValidationError> validate(Event event);
}
