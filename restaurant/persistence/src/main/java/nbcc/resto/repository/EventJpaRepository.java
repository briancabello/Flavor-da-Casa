package nbcc.resto.repository;

import nbcc.resto.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EventJpaRepository extends JpaRepository<EventEntity, Long> {
    boolean existsByNameIgnoreCase(String name);
    List<EventEntity> findByNameContainingIgnoreCaseAndStartDateGreaterThanEqualAndEndDateLessThanEqual(String name, LocalDateTime start, LocalDateTime end);
}
