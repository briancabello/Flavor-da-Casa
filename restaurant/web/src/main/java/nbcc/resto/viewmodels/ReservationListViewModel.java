package nbcc.resto.viewmodels;

import nbcc.resto.dto.EventDto;
import nbcc.resto.dto.ReservationDto;

import java.util.Collection;

public class ReservationListViewModel {

    private final Collection<EventDto> events;
    private final Collection<ReservationDto> reservations;
    private final boolean showManage;

    public ReservationListViewModel(Collection<EventDto> events,
                                    Collection<ReservationDto> reservations,
                                    boolean showManage) {
        this.events = events;
        this.reservations = reservations;
        this.showManage = showManage;
    }

    public Collection<EventDto> getEvents() {
        return events;
    }

    public Collection<ReservationDto> getReservations() {
        return reservations;
    }

    public boolean isShowManage() {
        return showManage;
    }

    public boolean isEmpty() {
        return reservations == null || reservations.isEmpty();
    }
}
