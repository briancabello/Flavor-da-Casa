package nbcc.resto.repository;

import nbcc.resto.dto.DiningTable;

import java.util.Collection;
import java.util.Optional;

public interface DiningTableRepository {

    Collection<DiningTable> getAll();

    Optional<DiningTable> get(Long id);

    DiningTable create(DiningTable table);

    DiningTable update(DiningTable table);

    void delete(Long id);

}
