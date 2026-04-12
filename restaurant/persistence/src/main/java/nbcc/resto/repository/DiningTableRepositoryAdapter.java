package nbcc.resto.repository;

import nbcc.resto.dto.DiningTable;
import nbcc.resto.mapper.DiningTableMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Repository
public class DiningTableRepositoryAdapter implements DiningTableRepository {

    private final DiningTableMapper mapper;
    private final DiningTableJpaRepository jpaRepository;
    private final SeatingTableJpaRepository seatingTableJpaRepository;

    public DiningTableRepositoryAdapter(
            DiningTableMapper mapper,
            DiningTableJpaRepository jpaRepository,
            SeatingTableJpaRepository seatingTableJpaRepository
    ) {
        this.mapper = mapper;
        this.jpaRepository = jpaRepository;
        this.seatingTableJpaRepository = seatingTableJpaRepository;
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
    @Transactional
    public void delete(Long id) {
        seatingTableJpaRepository.deleteByDiningTableId(id);
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean exists(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }
}
