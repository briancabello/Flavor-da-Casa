package nbcc.resto.viewmodels;

import nbcc.resto.dto.EventDto;
import nbcc.resto.dto.ReservationDto;
import nbcc.resto.dto.Seating;

import java.util.Collection;

public class ReservationListViewModel {

    private final Collection<EventDto> events;
    private final Collection<ReservationDto> reservations;
    private final Collection<Seating> seatings;
    private final boolean showManage;

    public ReservationListViewModel(Collection<EventDto> events,
                                    Collection<ReservationDto> reservations,
                                    Collection<Seating> seatings,
                                    boolean showManage) {
        this.events = events;
        this.reservations = reservations;
        this.seatings = seatings;
        this.showManage = showManage;
    }

    public Collection<EventDto> getEvents() {
        return events;
    }

    public Collection<ReservationDto> getReservations() {
        return reservations;
    }

    public Collection<Seating> getSeatings() {
        return seatings;
    }

    public Seating getSeatingById(Long id) {
        if (seatings == null || id == null) {
            return null;
        }

        for (var seating : seatings) {
            if (seating.getId().equals(id)) {
                return seating;
            }
        }

        return null;
    }

    public boolean isShowManage() {
        return showManage;
    }

    public boolean isEmpty() {
        return reservations == null || reservations.isEmpty();
    }
}
