package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.resto.dto.ReservationDto;

import java.util.Collection;
import java.util.UUID;

public interface ReservationService {

    Result<Collection<ReservationDto>> getAll();

    ValidatedResult<ReservationDto> get(long id);

    Result<Collection<ReservationDto>> getByEvent(long eventId);

    ValidatedResult<ReservationDto> getByUuid(UUID uuid);

    ValidatedResult<ReservationDto> requestReservation(ReservationDto reservation);

    ValidatedResult<ReservationDto> approve(long reservationId, long tableId);

    ValidatedResult<ReservationDto> deny(long reservationId);
    Result<Collection<ReservationDto>> getConfirmedByEvent(long eventId);

    boolean existsBySeatingId(long seatingId);

    boolean existsByEventId(long eventId);
}
