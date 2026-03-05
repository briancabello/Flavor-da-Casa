package nbcc.resto.viewmodels;

import nbcc.resto.dto.Event;

import java.util.Collection;

public class EventListViewModel {
    private final Collection<Event> events;
    private final boolean showManage;

    public EventListViewModel(Collection<Event> events, boolean showManage) {
        this.events = events;
        this.showManage = showManage;
    }
    public Collection<Event> getEvents() {
        return events;
    }

    public boolean isShowManage() {
        return showManage;
    }
}
