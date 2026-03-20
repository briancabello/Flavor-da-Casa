package nbcc.resto.service;

import nbcc.common.result.Result;
import nbcc.common.result.ValidatedResult;
import nbcc.common.result.ValidationResults;
import nbcc.common.validation.ValidationError;
import nbcc.resto.dto.ReservationDto;
import nbcc.resto.repository.EventRepository;
import nbcc.resto.repository.ReservationRepository;
import nbcc.resto.repository.SeatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final Logger logger = LoggerFactory.getLogger(ReservationServiceImpl.class);
    private final ReservationRepository reservationRepository;
    private final EventRepository eventRepository;
    private final SeatingRepository seatingRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  EventRepository eventRepository,
                                  SeatingRepository seatingRepository) {
        this.reservationRepository = reservationRepository;
        this.eventRepository = eventRepository;
        this.seatingRepository = seatingRepository;
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
            var errors = new ArrayList<ValidationError>();

            if (reservation.getEventId() == null) {
                errors.add(new ValidationError("Event is required", "eventId"));
            } else {
                var event = eventRepository.get(reservation.getEventId());
                if (event.isEmpty()) {
                    errors.add(new ValidationError("Selected event does not exist", "eventId"));
                }
            }

            if (reservation.getSeatingId() == null) {
                errors.add(new ValidationError("Seating is required", "seatingId"));
            } else {
                var seating = seatingRepository.get(reservation.getSeatingId());
                if (seating.isEmpty()) {
                    errors.add(new ValidationError("Selected seating does not exist", "seatingId"));
                } else if (reservation.getEventId() != null &&
                           !seating.get().getEventId().equals(reservation.getEventId())) {
                    errors.add(new ValidationError("Selected seating does not belong to the selected event", "seatingId"));
                }
            }

            if (reservation.getGuestFirstName() == null || reservation.getGuestFirstName().isBlank()) {
                errors.add(new ValidationError("First name is required", "guestFirstName"));
            }

            if (reservation.getGuestLastName() == null || reservation.getGuestLastName().isBlank()) {
                errors.add(new ValidationError("Last name is required", "guestLastName"));
            }

            if (reservation.getEmail() == null || reservation.getEmail().isBlank()) {
                errors.add(new ValidationError("Email is required", "email"));
            }

            if (reservation.getGroupSize() == null || reservation.getGroupSize() < 1) {
                errors.add(new ValidationError("Group size must be at least 1", "groupSize"));
            }

            if (!errors.isEmpty()) {
                logger.debug("Validation errors for reservation request: {}", errors);
                return ValidationResults.invalid(reservation, errors);
            }

            reservation.setUuid(UUID.randomUUID());
            reservation.setStatus("pending");

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
            return ValidationResults.success(reservationRepository.updateStatus(reservationId, "approved", tableId));
        } catch (Exception e) {
            logger.error("Error approving reservation {}", reservationId, e);
            return ValidationResults.error(e);
        }
    }

    @Override
    public ValidatedResult<ReservationDto> deny(long reservationId) {
        try {
            return ValidationResults.success(reservationRepository.updateStatus(reservationId, "denied", null));
        } catch (Exception e) {
            logger.error("Error denying reservation {}", reservationId, e);
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
