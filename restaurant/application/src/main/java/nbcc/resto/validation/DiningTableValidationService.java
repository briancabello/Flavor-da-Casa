package nbcc.resto.validation;

import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.DiningTable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class DiningTableValidationService {

    public Collection<ValidationError> validate(DiningTable diningTable) {
        var errors = new ArrayList<ValidationError>();

        if (diningTable.getCapacity() <= 0) {
            errors.add(new ValidationError("Capacity must be greater than 0", "capacity", diningTable.getCapacity()));
        }

        return errors;
    }
}
