package nbcc.resto.repository;

import nbcc.resto.dto.DiningTable;
import nbcc.resto.dto.Seating;
import nbcc.resto.entity.SeatingTableEntity;
import nbcc.resto.mapper.DiningTableMapper;
import nbcc.resto.mapper.SeatingMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Repository
public class SeatingRepositoryAdapter implements SeatingRepository {

    private final SeatingMapper seatingMapper;
    private final DiningTableMapper diningTableMapper;
    private final SeatingJpaRepository seatingJpa;
    private final SeatingTableJpaRepository seatingTableJpa;
    private final DiningTableJpaRepository diningTableJpa;

    public SeatingRepositoryAdapter(
            SeatingMapper seatingMapper,
            DiningTableMapper diningTableMapper,
            SeatingJpaRepository seatingJpa,
            SeatingTableJpaRepository seatingTableJpa,
            DiningTableJpaRepository diningTableJpa
    ) {
        this.seatingMapper = seatingMapper;
        this.diningTableMapper = diningTableMapper;
        this.seatingJpa = seatingJpa;
        this.seatingTableJpa = seatingTableJpa;
        this.diningTableJpa = diningTableJpa;
    }

    @Override
    public Collection<Seating> getAll() {
        var seatings = seatingMapper.toDTO(seatingJpa.findAll());

        for (Seating seating : seatings) {
            seating.setTables(getTablesForSeating(seating.getId()));
        }

        return seatings;
    }

    @Override
    public Collection<Seating> getByEvent(Long eventId) {
        var seatings = seatingMapper.toDTO(seatingJpa.findByEventId(eventId));

        for (Seating seating : seatings) {
            seating.setTables(getTablesForSeating(seating.getId()));
        }

        return seatings;
    }

    @Override
    public Collection<Seating> getSeatingsByTableId(Long tableId) {
        var entities = seatingTableJpa.findSeatingsByTableId(tableId);
        return entities.stream()
                .map(seatingMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public Seating create(Seating seating) {
        var entity = seatingMapper.toEntity(seating);
        var savedEntity = seatingJpa.save(entity);

        if (seating.getSelectedTableIds() != null) {
            for (Long tableId : seating.getSelectedTableIds()) {
                var tableEntity = diningTableJpa.findById(tableId).orElse(null);
                if (tableEntity != null) {
                    seatingTableJpa.save(new SeatingTableEntity(savedEntity, tableEntity));
                }
            }
        }

        return seatingMapper.toDTO(savedEntity);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        seatingJpa.deleteById(id);
    }

    private Collection<DiningTable> getTablesForSeating(Long seatingId) {
        return seatingTableJpa.findBySeatingId(seatingId).stream()
                .map(st -> diningTableMapper.toDTO(st.getDiningTable()))
                .toList();
    }
}
