package nbcc.resto.repository;

import nbcc.resto.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findBySeatingId(long seatingId);

    List<ReservationEntity> findByEventId(long eventId);

    Optional<ReservationEntity> findByUuid(UUID uuid);

    boolean existsBySeatingId(long seatingId);

    boolean existsByEventId(long eventId);

    boolean existsBySeatingIdAndAssignedTableIdAndStatus(long seatingId, long assignedTableId, String status);
}
