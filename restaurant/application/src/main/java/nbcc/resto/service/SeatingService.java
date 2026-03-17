package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.Seating;

import java.util.Collection;

public interface SeatingService {

    Result<Collection<Seating>> getAll();

    Result<Collection<Seating>> getByEvent(Long eventId);

    Result<Collection<Seating>> getSeatingsByTableId(Long tableId);

    ValidatedResult<Seating> create(Seating seating);

    Result<Seating> get(Long id);

    ValidatedResult<Seating> update(Seating seating);

    ValidatedResult<Seating> delete(Long id);

}
