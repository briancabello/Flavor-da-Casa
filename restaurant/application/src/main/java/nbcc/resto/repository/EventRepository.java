package nbcc.resto.repository;

import nbcc.resto.dto.EventDto;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface EventRepository {
    Collection<EventDto> getAll();
    Optional<EventDto> get(Long id);
    EventDto create(EventDto eventDto);
    EventDto update(EventDto eventDto);
    void delete(Long id);
    Collection<EventDto> search(String name, LocalDateTime start, LocalDateTime end);
    boolean exists(String name);
}
