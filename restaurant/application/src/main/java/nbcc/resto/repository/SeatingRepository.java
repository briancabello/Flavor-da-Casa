package nbcc.resto.repository;

import nbcc.resto.dto.Seating;

import java.util.Collection;
import java.util.Optional;

public interface SeatingRepository {

    Collection<Seating> getAll();

    Collection<Seating> getByEvent(Long eventId);

    Collection<Seating> getSeatingsByTableId(Long tableId);

    Seating create(Seating seating);

    Optional<Seating> get(Long id);

    Seating update(Seating seating);

    void delete(Long id);

}
