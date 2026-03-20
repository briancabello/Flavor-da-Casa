package nbcc.resto.repository;

import nbcc.resto.dto.ReservationDto;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    Collection<ReservationDto> getByEvent(long eventId);
    Optional<ReservationDto> getByUuid(UUID uuid);
    ReservationDto create(ReservationDto reservation);
    ReservationDto updateStatus(long id, String status, Long tableId);
    boolean existsBySeatingId(long seatingId);
}
