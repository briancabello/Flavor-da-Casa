package nbcc.resto.mapper;

import nbcc.resto.dto.ReservationDto;
import nbcc.resto.entity.ReservationEntity;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper implements EntityMapper<ReservationDto, ReservationEntity> {
    @Override
    public ReservationDto toDTO(ReservationEntity entity) {
        if (entity == null) return null;

        ReservationDto dto = new ReservationDto();
        dto.setId(entity.getId())
        .setUuid(entity.getUuid())
        .setEventId(entity.getEventId())
        .setSeatingId(entity.getSeatingId())
        .setGuestFirstName(entity.getGuestFirstName())
        .setGuestLastName(entity.getGuestLastName())
        .setEmail(entity.getEmail())
        .setGroupSize(entity.getGroupSize())
        .setStatus(entity.getStatus())
        .setAssignedTableId(entity.getAssignedTableId());
        return dto;
    }

    @Override
    public ReservationEntity toEntity(ReservationDto dto) {
        if (dto == null) return null;

        ReservationEntity entity = new ReservationEntity();

        entity.setId(dto.getId());
        entity.setUuid(dto.getUuid());
        entity.setEventId(dto.getEventId());
        entity.setSeatingId(dto.getSeatingId());
        entity.setGuestFirstName(dto.getGuestFirstName());
        entity.setGuestLastName(dto.getGuestLastName());
        entity.setEmail(dto.getEmail());
        entity.setGroupSize(dto.getGroupSize());
        entity.setStatus(dto.getStatus());
        entity.setAssignedTableId(dto.getAssignedTableId());
        return entity;
    }
}
