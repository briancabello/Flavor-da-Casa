package nbcc.resto.repository;

import nbcc.resto.dto.Event;
import nbcc.resto.entity.EventEntity;
import nbcc.resto.mapper.EventMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Component
public class EventRepositoryAdapter implements EventRepository {

    private final EventJpaRepository jpaRepository;
    private final EventMapper mapper;

    public EventRepositoryAdapter(EventJpaRepository jpaRepository, EventMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }


    @Override
    public Collection<Event> getAll() {
        return mapper.toDomain(jpaRepository.findAll());
    }

    @Override
    public Optional<Event> get(Long id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Event create(Event event) {
        EventEntity entity = mapper.toEntity(event);
        entity.setCreatedDate(LocalDateTime.now());
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public Event update(Event event) {
        EventEntity entity = mapper.toEntity(event);
        entity.setLastUpdatedDate(LocalDateTime.now());
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Collection<Event> search(String name, LocalDateTime start, LocalDateTime end) {
        return mapper.toDomain(jpaRepository.findByNameContainingIgnoreCaseAndStartDateGreaterThanEqualAndEndDateLessThanEqual(name, start, end));
    }

    @Override
    public boolean exists(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }
}
