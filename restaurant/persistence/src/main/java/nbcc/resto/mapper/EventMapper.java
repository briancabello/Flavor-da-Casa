package nbcc.resto.mapper;

import nbcc.resto.dto.Event;
import nbcc.resto.entity.EventEntity;

import java.time.LocalDateTime;

public class EventMapper  implements EntityMapper<Event, EventEntity>{
    @Override
    public EventEntity toEntity(Event domain) {
        if (domain == null) return null;
        EventEntity entity = new EventEntity();
        entity.setId(domain.getId())
                .setName(domain.getName())
                .setDescription(domain.getDescription())
                .setStartDate(domain.getStartDate())
                .setEndDate(domain.getEndDate())
                .setDuration(domain.getDuration())
                .setPrice(domain.getPrice())
                .setActive(domain.isActive())
                .setCreatedDate(domain.getCreatedDate())
                .setLastUpdatedDate(domain.getLastUpdatedDate());
        return entity;
    }

    @Override
    public Event toDomain(EventEntity entity) {
        if (entity == null) return null;
        Event domain = new Event();
        domain.setId(entity.getId())
                .setMenuId(entity.getMenuId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setStartDate(entity.getStartDate())
                .setEndDate(entity.getEndDate())
                .setDuration(entity.getDuration())
                .setPrice(entity.getPrice())
                .setActive(entity.isActive())
                .setCreatedDate(entity.getCreatedDate())
                .setLastUpdatedDate(entity.getLastUpdatedDate());
        return domain;

    }
}
