package nbcc.resto.repository;

import nbcc.resto.dto.ReservationStatus;
import nbcc.resto.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    @Query("SELECT r FROM ReservationEntity r JOIN SeatingEntity s ON r.seatingId = s.id ORDER BY s.startDateTime ASC")
    List<ReservationEntity> findAllOrderBySeatingStartDateTime();

    List<ReservationEntity> findBySeatingId(long seatingId);

    @Query("SELECT r FROM ReservationEntity r JOIN SeatingEntity s ON r.seatingId = s.id WHERE r.eventId = :eventId ORDER BY s.startDateTime ASC")
    List<ReservationEntity> findByEventIdOrderBySeatingStartDateTime(@Param("eventId") long eventId);

    Optional<ReservationEntity> findByUuid(UUID uuid);

    @Query("SELECT r FROM ReservationEntity r JOIN SeatingEntity s ON r.seatingId = s.id WHERE r.eventId = :eventId AND r.status = 'APPROVED' ORDER BY s.startDateTime ASC")
    List<ReservationEntity> findConfirmedByEventId(@Param("eventId") long eventId);

    boolean existsBySeatingId(long seatingId);

    boolean existsByEventId(long eventId);

    boolean existsBySeatingIdAndAssignedTableIdAndStatus(long seatingId, long assignedTableId, ReservationStatus status);
}
