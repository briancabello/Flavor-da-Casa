package nbcc.resto.repository;

import nbcc.resto.dto.Event;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

public interface EventRepository {
    Collection<Event> getAll();
    Optional<Event> get(Long id);
    Event create(Event event);
    Event update(Event event);
    void delete(Long id);
    Collection<Event> search(String name, LocalDateTime start, LocalDateTime end);
    boolean exists(String name);
}
