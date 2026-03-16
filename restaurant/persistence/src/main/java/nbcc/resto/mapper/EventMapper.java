package nbcc.resto.mapper;

import nbcc.resto.dto.EventDto;
import nbcc.resto.entity.EventEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class EventMapper implements EntityMapper<EventDto, EventEntity>{

    @Override
    public EventEntity toEntity(EventDto dto) {
        if (dto == null) return null;
        EventEntity entity = new EventEntity();
        entity.setId(dto.getId())
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setStartDate(dto.getStartDate() != null ? dto.getStartDate().atStartOfDay() : null)
                .setEndDate(dto.getEndDate() != null ? dto.getEndDate().atStartOfDay() : null)
                .setDuration(dto.getDuration())
                .setPrice(dto.getPrice())
                .setActive(dto.isActive())
                .setArchived(dto.isArchived())
                .setCreatedDate(dto.getCreatedDate())
                .setLastUpdatedDate(dto.getLastUpdatedDate());
        return entity;
    }

    @Override
    public EventDto toDTO(EventEntity entity) {
        if (entity == null) return null;
        EventDto domain = new EventDto();
        domain.setId(entity.getId())
                //.setMenuId(entity.getMenuId())
                .setName(entity.getName())
                .setDescription(entity.getDescription())
                .setStartDate(entity.getStartDate() != null ? entity.getStartDate().toLocalDate() : null)
                .setEndDate(entity.getEndDate() != null ? entity.getEndDate().toLocalDate() : null)
                .setDuration(entity.getDuration())
                .setPrice(entity.getPrice())
                .setActive(entity.isActive())
                .setArchived(entity.isArchived())
                .setCreatedDate(entity.getCreatedDate())
                .setLastUpdatedDate(entity.getLastUpdatedDate());
        return domain;

    }
}
