package nbcc.resto.repository;

import nbcc.resto.dto.ReservationDto;
import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.mapper.ReservationMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ReservationRepositoryAdapter implements ReservationRepository {

    private final ReservationJpaRepository jpaRepository;
    private final ReservationMapper mapper;

    public ReservationRepositoryAdapter(ReservationJpaRepository jpaRepository, ReservationMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Collection<ReservationDto> getAll() {
        return mapper.toDTO(jpaRepository.findAllOrderBySeatingStartDateTime());
    }

    @Override
    public Optional<ReservationDto> get(long id) {
        return jpaRepository.findById(id).map(mapper::toDTO);
    }

    @Override
    public Collection<ReservationDto> getByEvent(long eventId) {
        return mapper.toDTO(jpaRepository.findByEventIdOrderBySeatingStartDateTime(eventId));
    }

    @Override
    public Optional<ReservationDto> getByUuid(UUID uuid) {
        return jpaRepository.findByUuid(uuid).map(mapper::toDTO);
    }

    @Override
    public ReservationDto create(ReservationDto reservation) {
        var entity = mapper.toEntity(reservation);
        return mapper.toDTO(jpaRepository.save(entity));
    }

    @Override
    public ReservationDto updateStatus(long id, ReservationStatus status, Long tableId) {
        var entity = jpaRepository.findById(id).orElseThrow();
        entity.setStatus(status);
        entity.setAssignedTableId(tableId);
        return mapper.toDTO(jpaRepository.save(entity));
    }

    @Override
    public Collection<ReservationDto> getConfirmedByEvent(long eventId) {
        return mapper.toDTO(jpaRepository.findConfirmedByEventId(eventId));
    }

    @Override
    public boolean existsBySeatingId(long seatingId) {
        return jpaRepository.existsBySeatingId(seatingId);
    }

    @Override
    public boolean existsByEventId(long eventId) {
        return jpaRepository.existsByEventId(eventId);
    }

    @Override
    public boolean isTableAssignedForSeating(long seatingId, long tableId) {
        return jpaRepository.existsBySeatingIdAndAssignedTableIdAndStatus(seatingId, tableId, ReservationStatus.APPROVED);
    }
}
