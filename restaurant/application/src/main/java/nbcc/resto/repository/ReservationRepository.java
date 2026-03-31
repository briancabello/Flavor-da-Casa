package nbcc.resto.repository;

import nbcc.resto.dto.ReservationDto;
import nbcc.resto.dto.ReservationStatus;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {
    Collection<ReservationDto> getAll();
    Optional<ReservationDto> get(long id);
    Collection<ReservationDto> getByEvent(long eventId);
    Optional<ReservationDto> getByUuid(UUID uuid);
    ReservationDto create(ReservationDto reservation);
    ReservationDto updateStatus(long id, ReservationStatus status, Long tableId);
    boolean existsBySeatingId(long seatingId);
    boolean existsByEventId(long eventId);
    boolean isTableAssignedForSeating(long seatingId, long tableId);
}
