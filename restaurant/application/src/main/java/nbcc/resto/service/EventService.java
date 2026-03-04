package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Event;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventService {
    Result<Collection<Event>> getAll();
    ValidatedResult<Event> get(Long id);
    ValidatedResult<Event> create(Event event);
    ValidatedResult<Event> update(Event event);
    ValidatedResult<Event> delete(Long id);
    Result<Collection<Event>> search(String query, LocalDateTime start, LocalDateTime end);
}
