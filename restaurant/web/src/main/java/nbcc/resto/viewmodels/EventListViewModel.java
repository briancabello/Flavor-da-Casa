package nbcc.resto.viewmodels;

import nbcc.resto.dto.EventDto;

import java.util.Collection;

public class EventListViewModel {
    private final Collection<EventDto> eventDtos;
    private final boolean showManage;

    public EventListViewModel(Collection<EventDto> eventDtos, boolean showManage) {
        this.eventDtos = eventDtos;
        this.showManage = showManage;
    }
    public Collection<EventDto> getEvents() {
        return eventDtos;
    }

    public boolean isShowManage() {
        return showManage;
    }
}
