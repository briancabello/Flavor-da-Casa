package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.resto.dto.ReservationDto;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.repository.ReservationRepository;
import nbcc.resto.validation.ReservationValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);
    private final ReservationRepository reservationRepository;
    private final ReservationValidationService validationService;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  ReservationValidationService validationService) {
        this.reservationRepository = reservationRepository;
        this.validationService = validationService;
    }

    @Override
    public Result<Collection<ReservationDto>> getAll() {
        try {
            return ValidationResults.success(reservationRepository.getAll());
        } catch (Exception e) {
            logger.error("Error retrieving all reservations", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<ReservationDto> get(long id) {
        try {
            var reservation = reservationRepository.get(id);
            if (reservation.isEmpty()) {
                return ValidationResults.invalid(null, "Reservation not found", "id");
            }
            return ValidationResults.success(reservation.get());
        } catch (Exception e) {
            logger.error("Error retrieving reservation with id {}", id, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<ReservationDto>> getByEvent(long eventId) {
        try {
            return ValidationResults.success(reservationRepository.getByEvent(eventId));
        } catch (Exception e) {
            logger.error("Error retrieving reservations for event {}", eventId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<ReservationDto> getByUuid(UUID uuid) {
        try {
            var reservation = reservationRepository.getByUuid(uuid);
            if (reservation.isEmpty()) {
                return ValidationResults.invalid(null, "Reservation not found", "uuid");
            }
            return ValidationResults.success(reservation.get());
        } catch (Exception e) {
            logger.error("Error retrieving reservation by UUID {}", uuid, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<ReservationDto> requestReservation(ReservationDto reservation) {
        try {
            var errors = validationService.validate(reservation);

            if (!errors.isEmpty()) {
                logger.debug("Validation errors for reservation request: {}", errors);
                return ValidationResults.invalid(reservation, errors);
            }

            reservation.setUuid(UUID.randomUUID());
            reservation.setStatus(ReservationStatus.PENDING);

            var created = reservationRepository.create(reservation);
            return ValidationResults.success(created);

        } catch (Exception e) {
            logger.error("Error creating reservation request", e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<ReservationDto> approve(long reservationId, long tableId) {
        try {
            var optional = reservationRepository.get(reservationId);
            if (optional.isEmpty()) {
                return ValidationResults.invalid(null, "Reservation not found", "id");
            }

            var reservation = optional.get();

            if (ReservationStatus.APPROVED == reservation.getStatus()) {
                return ValidationResults.invalid(reservation,
                        "An approved reservation cannot have its status changed", "status");
            }

            if (reservationRepository.isTableAssignedForSeating(reservation.getSeatingId(), tableId)) {
                return ValidationResults.invalid(reservation,
                        "This table is already assigned to another approved reservation for this seating", "assignedTableId");
            }

            return ValidationResults.success(reservationRepository.updateStatus(reservationId, ReservationStatus.APPROVED, tableId));
        } catch (Exception e) {
            logger.error("Error approving reservation {}", reservationId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<ReservationDto> deny(long reservationId) {
        try {
            var optional = reservationRepository.get(reservationId);
            if (optional.isEmpty()) {
                return ValidationResults.invalid(null, "Reservation not found", "id");
            }

            var reservation = optional.get();

            if (ReservationStatus.APPROVED == reservation.getStatus()) {
                return ValidationResults.invalid(reservation,
                        "An approved reservation cannot have its status changed", "status");
            }

            return ValidationResults.success(reservationRepository.updateStatus(reservationId, ReservationStatus.DENIED, null));
        } catch (Exception e) {
            logger.error("Error denying reservation {}", reservationId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public Result<Collection<ReservationDto>> getConfirmedByEvent(long eventId) {
        try {
            return ValidationResults.success(reservationRepository.getConfirmedByEvent(eventId));
        } catch (Exception e) {
            logger.error("Error retrieving confirmed reservations for event {}", eventId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public boolean existsBySeatingId(long seatingId) {
        return reservationRepository.existsBySeatingId(seatingId);
    }

    @Override
    public boolean existsByEventId(long eventId) {
        return reservationRepository.existsByEventId(eventId);
    }
}
