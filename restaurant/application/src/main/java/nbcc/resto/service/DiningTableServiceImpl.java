package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.DiningTable;
import nbcc.resto.repository.DiningTableRepository;
import nbcc.resto.validation.DiningTableValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class DiningTableServiceImpl implements DiningTableService {

    private final Logger logger = LoggerFactory.getLogger(DiningTableServiceImpl.class);

    private final DiningTableRepository diningTableRepository;
    private final DiningTableValidationService validationService;

    public DiningTableServiceImpl(DiningTableRepository diningTableRepository, DiningTableValidationService validationService) {
        this.diningTableRepository = diningTableRepository;
        this.validationService = validationService;
    }

    @Override
    public Result<Collection<DiningTable>> getAll() {
        try {
            return ValidationResults.success(diningTableRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all dining tables", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<DiningTable> get(Long id) {
        try {
            return ValidationResults.success(diningTableRepository.get(id));
        } catch (Exception e) {
            logger.error("Error retrieving dining table with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<DiningTable> create(DiningTable table) {
        try {
            var errors = validationService.validate(table);

            if (errors.isEmpty()) {
                return ValidationResults.success(diningTableRepository.create(table));
            } else {
                logger.debug("Validation errors for table create {}: {}", table, errors);
                return ValidationResults.invalid(table, errors);
            }
        } catch (Exception e) {
            logger.error("Error creating dining table {}", table, e);
            return ValidationResults.error(table);
        }
    }

    @Override
    public ValidatedResult<DiningTable> update(DiningTable table) {
        try {
            var errors = validationService.validate(table);

            if (errors.isEmpty()) {
                return ValidationResults.success(diningTableRepository.update(table));
            }

            logger.debug("Validation errors for table update {}: {}", table, errors);
            return ValidationResults.invalid(table, errors);

        } catch (Exception e) {
            logger.error("Error updating dining table {}", table, e);
            return ValidationResults.error(table);
        }
    }

    @Override
    public ValidatedResult<Void> delete(Long id) {
        try {
            diningTableRepository.delete(id);
            logger.debug("Dining table with id {} deleted", id);
            return ValidationResults.success();
        } catch (Exception e) {
            logger.error("Error deleting dining table with id: {}", id, e);
            return ValidationResults.error(e);
        }
    }
}
