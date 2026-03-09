package nbcc.resto.repository;

import nbcc.resto.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
    List<EventEntity> findByNameContainingIgnoreCaseAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String name, LocalDateTime start, LocalDateTime end);
    List<EventEntity> findByArchivedFalse();

    @Query("SELECT e FROM EventEntity e WHERE e.archived = false " +
            "AND (:name IS NULL OR :name = '' OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
            "AND (:startDate IS NULL OR e.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR e.endDate <= :endDate)")
    List<EventEntity> searchEvents(@Param("name") String name,
                                   @Param("startDate") LocalDateTime startDate,
                                   @Param("endDate") LocalDateTime endDate);

    
}
