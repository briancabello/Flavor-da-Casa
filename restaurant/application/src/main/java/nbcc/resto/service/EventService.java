package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.EventDto;

import java.time.LocalDate;
import java.util.Collection;

public interface EventService {
    Result<Collection<EventDto>> getAll();
    ValidatedResult<EventDto> get(Long id);
    ValidatedResult<EventDto> create(EventDto eventDto);
    ValidatedResult<EventDto> update(EventDto eventDto);
    ValidatedResult<EventDto> delete(Long id);
    ValidatedResult<Collection<EventDto>> search(String query, LocalDate start, LocalDate end);
}
