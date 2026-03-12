package nbcc.resto.repository;

import nbcc.resto.dto.Seating;

import java.util.Collection;

public interface SeatingRepository {

    Collection<Seating> getAll();

    Collection<Seating> getByEvent(Long eventId);

    Collection<Seating> getSeatingsByTableId(Long tableId);

    Seating create(Seating seating);

    //void delete(Long id);

}
