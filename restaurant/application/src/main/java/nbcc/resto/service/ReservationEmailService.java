package nbcc.resto.service;

import nbcc.resto.dto.ReservationDto;

public interface ReservationEmailService {
    void sendReservationReceived(ReservationDto reservation);
    void sendReservationStatusUpdate(ReservationDto reservation);
}
