package nbcc.resto.repository;

import nbcc.resto.dto.DiningTable;
import nbcc.resto.mapper.DiningTableMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class DiningTableRepositoryAdapter implements DiningTableRepository {

    private final DiningTableMapper mapper;
    private final DiningTableJpaRepository jpaRepository;

    public DiningTableRepositoryAdapter(DiningTableMapper mapper, DiningTableJpaRepository jpaRepository) {
        this.mapper = mapper;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Collection<DiningTable> getAll() {
        var entities = jpaRepository.findAll();
        return mapper.toDTO(entities);
    }

    @Override
    public Optional<DiningTable> get(Long id) {
        var entity = jpaRepository.findById(id);
        return mapper.toDTO(entity);
    }

    @Override
    public DiningTable create(DiningTable table) {
        var entity = mapper.toEntity(table);
        entity = jpaRepository.save(entity);

        // Auto-generate name if blank: "Table {id}"
        if (entity.getName() == null || entity.getName().isBlank()) {
            entity.setName("Table " + entity.getId());
            entity = jpaRepository.save(entity);
        }

        return mapper.toDTO(entity);
    }

    @Override
    public DiningTable update(DiningTable table) {
        var entity = mapper.toEntity(table);

        // Auto-generate name if blank: "Table {id}"
        if (entity.getName() == null || entity.getName().isBlank()) {
            entity.setName("Table " + entity.getId());
        }

        entity = jpaRepository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }
}
