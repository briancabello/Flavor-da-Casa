package nbcc.resto.mapper;

import nbcc.resto.dto.Seating;
import nbcc.resto.entity.SeatingEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class SeatingMapper implements EntityMapper<Seating, SeatingEntity> {

    @Override
    public Seating toDTO(SeatingEntity entity) {
        if (entity == null) return null;
        return new Seating()
                .setId(entity.getId())
                .setEventId(entity.getEventId())
                .setName(entity.getName())
                .setStartDateTime(entity.getStartDateTime())
                .setDuration(entity.getDuration())
                .setCreatedDate(entity.getCreatedDate())
                .setStatus(entity.isStatus())
                .setUpdatedDate(entity.getUpdatedDate());
    }

    @Override
    public SeatingEntity toEntity(Seating dto) {
        if (dto == null) return null;
        return new SeatingEntity()
                .setId(dto.getId())
                .setEventId(dto.getEventId())
                .setName(dto.getName())
                .setStartDateTime(dto.getStartDateTime())
                .setDuration(dto.getDuration() != null ? dto.getDuration() : 0)
                .setCreatedDate(dto.getCreatedDate())
                .setStatus(dto.isStatus())
                .setUpdatedDate(dto.getUpdatedDate());
    }
}
