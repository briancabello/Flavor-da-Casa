package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.resto.dto.Event;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventService {
    Result<Collection<Event>> getAll();
    Result<Event> get(Long id);
    Result<Event> create(Event event);
    Result<Event> update(Event event);
    Result<Event> delete(Long id);
    Result<Collection<Event>> search(String query, LocalDateTime start, LocalDateTime end);
}
