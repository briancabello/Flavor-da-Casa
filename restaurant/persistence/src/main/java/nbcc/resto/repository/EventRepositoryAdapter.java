package nbcc.resto.repository;

import nbcc.resto.dto.EventDto;
import nbcc.resto.entity.EventEntity;
import nbcc.resto.mapper.EventMapper;
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
    public Collection<EventDto> getAll() {
        return jpaRepository.findByArchivedFalse().stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public Optional<EventDto> get(Long id) {
        return jpaRepository.findById(id).map(mapper::toDTO);
    }

    @Override
    public EventDto create(EventDto eventDto) {
        EventEntity entity = mapper.toEntity(eventDto);
        entity.setCreatedDate(LocalDateTime.now());
        return mapper.toDTO(jpaRepository.save(entity));
    }

    @Override
    public EventDto update(EventDto eventDto) {
        EventEntity entity = mapper.toEntity(eventDto);
        entity.setLastUpdatedDate(LocalDateTime.now());
        return mapper.toDTO(jpaRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Collection<EventDto> search(String name, LocalDateTime start, LocalDateTime end) {
        return mapper.toDTO(jpaRepository.searchEvents(name, start, end));
    }


    @Override
    public boolean exists(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }
}
