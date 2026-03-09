package nbcc.resto.repository;

import nbcc.resto.dto.Event;
import nbcc.resto.entity.EventEntity;
import nbcc.resto.mapper.EventMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Repository
public class EventRepositoryAdapter implements EventRepository {

    private final EventJpaRepository jpaRepository;
    private final EventMapper mapper;

    public EventRepositoryAdapter(EventJpaRepository jpaRepository, EventMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }


    @Override
    public Collection<Event> getAll() {
        return jpaRepository.findByArchivedFalse().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Optional<Event> get(Long id) {
        return jpaRepository.findById(id).map(mapper::toDTO);
    }

    @Override
    public Event create(Event event) {
        EventEntity entity = mapper.toEntity(event);
        entity.setCreatedDate(LocalDateTime.now());
        return mapper.toDTO(jpaRepository.save(entity));
    }

    @Override
    public Event update(Event event) {
        EventEntity entity = mapper.toEntity(event);
        entity.setLastUpdatedDate(LocalDateTime.now());
        return mapper.toDTO(jpaRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Collection<Event> search(String name, LocalDateTime start, LocalDateTime end) {
        return mapper.toDTO(jpaRepository.searchEvents(name, start, end));
    }


    @Override
    public boolean exists(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }
}
