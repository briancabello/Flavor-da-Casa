package nbcc.resto.service;

import nbcc.email.domain.EmailRequest;
import nbcc.email.service.EmailService;
import nbcc.resto.dto.ReservationDto;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.repository.EventRepository;
import nbcc.resto.repository.SeatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class ReservationEmailServiceImpl implements ReservationEmailService {

    private final Logger logger = LoggerFactory.getLogger(ReservationEmailServiceImpl.class);
    private final EmailService emailService;
    private final EventRepository eventRepository;
    private final SeatingRepository seatingRepository;

    public ReservationEmailServiceImpl(EmailService emailService,
                                       EventRepository eventRepository,
                                       SeatingRepository seatingRepository) {
        this.emailService = emailService;
        this.eventRepository = eventRepository;
        this.seatingRepository = seatingRepository;
    }

    @Override
    public void sendReservationReceived(ReservationDto reservation) {
        var body = buildEmailBody(reservation,
                "Your reservation request has been received and is currently under review.",
                "Please save your reservation code. You will need it to check your reservation details and status.\n"
                          + "You will be notified once your reservation has been approved or declined.");
        var email = new EmailRequest()
                .setTo(reservation.getEmail())
                .setSubject("Reservation Request Received - Flavor da Casa")
                .setBody(body)
                .setHtml(false);

        if (!emailService.sendEmail(email)) {
            logger.warn("Failed to send reservation received email to {}", reservation.getEmail());
        }
    }

    @Override
    public void sendReservationStatusUpdate(ReservationDto reservation) {
        var statusText = reservation.getStatus().name().toLowerCase();
        String message;
        String closing;

        if (reservation.getStatus() == ReservationStatus.APPROVED) {
            message = "Great news! Your reservation has been approved.";
            closing = "Please keep your reservation code. You may need it to check your reservation details.\n"
                    + "We look forward to serving you!";
        } else {
            message = "Unfortunately, your reservation has been denied.";
            closing = "You may submit a new reservation request if you'd like to try again.";
        }

        var body = buildEmailBody(reservation, message, closing);

        var email = new EmailRequest()
                .setTo(reservation.getEmail())
                .setSubject("Reservation " + capitalize(statusText) + " - Flavor da Casa")
                .setBody(body)
                .setHtml(false);

        if (!emailService.sendEmail(email)) {
            logger.warn("Failed to send reservation status email to {}", reservation.getEmail());
        }
    }

    private String buildEmailBody(ReservationDto reservation, String message, String closing) {
        var sb = new StringBuilder();
        sb.append("Dear ").append(reservation.getGuestFirstName())
                .append(" ").append(reservation.getGuestLastName()).append(",\n\n");
        sb.append(message).append("\n\n");
        sb.append("Reservation Details:\n");
        sb.append("----------------------------\n");
        sb.append("Confirmation number: ").append(reservation.getUuid()).append("\n");

        // Event Name
        var eventOpt = eventRepository.get(reservation.getEventId());
        eventOpt.ifPresent(event ->
                sb.append("Event: ").append(event.getName()).append("\n"));

        // Seating Date and Time
        var seatingOpt = seatingRepository.get(reservation.getSeatingId());
        seatingOpt.ifPresent(seating ->
                sb.append("Seating: ").append(seating.getStartDateTime()
                                .format(DateTimeFormatter.ofPattern("MMMM dd, yyyy h:mm a")))
                        .append("\n"));

        sb.append("Guest: ").append(reservation.getGuestFirstName())
                .append(" ").append(reservation.getGuestLastName()).append("\n");
        sb.append("Group Size: ").append(reservation.getGroupSize()).append("\n");
        sb.append("Status: ").append(reservation.getStatus().name()).append("\n");
        sb.append("----------------------------\n\n");
        sb.append(closing).append("\n\n");
        sb.append("Thank you,\nFlavor da Casa");

        return sb.toString();
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}