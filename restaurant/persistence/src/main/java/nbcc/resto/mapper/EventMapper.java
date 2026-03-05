package nbcc.resto.mapper;

import nbcc.resto.dto.Event;
import nbcc.resto.entity.EventEntity;
import org.springframework.stereotype.Component;

@Component
public class EventMapper implements EntityMapper<Event, EventEntity>{

    @Override
    public EventEntity toEntity(Event dto) {
        if (dto == null) return null;
        EventEntity entity = new EventEntity();
        entity.setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setStartDate(dto.getStartDate())
                .setEndDate(dto.getEndDate())
                .setDuration(dto.getDuration())
                .setPrice(dto.getPrice())
                .setActive(dto.isActive())
                .setCreatedDate(dto.getCreatedDate())
                .setLastUpdatedDate(dto.getLastUpdatedDate());
        return entity;
    }

    @Override
    public Event toDTO(EventEntity entity) {
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
