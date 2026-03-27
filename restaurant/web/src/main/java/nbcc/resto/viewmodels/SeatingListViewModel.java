package nbcc.resto.viewmodels;

import nbcc.resto.dto.EventDto;
import nbcc.resto.dto.Seating;

import java.util.Collection;

public class SeatingListViewModel {

    private final Collection<EventDto> events;
    private final Collection<Seating> seatings;
    private final boolean canAdd;
    private String message;

    public SeatingListViewModel(Collection<EventDto> events, Collection<Seating> seatings, boolean canAdd) {
        this.events = events;
        this.seatings = seatings;
        this.canAdd = canAdd;
    }

    public Collection<EventDto> getEvents() {
        return events;
    }

    public Collection<Seating> getSeatings() {
        return seatings;
    }

    public boolean isCanAdd() {
        return canAdd;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isEmpty() {
        return events == null || events.isEmpty();
    }

    public boolean hasSeatings(Long eventId) {
        if (seatings == null || seatings.isEmpty()) {
            return false;
        }

        return seatings.stream().anyMatch(s -> s.getEventId().equals(eventId) && s.isStatus());
    }
}