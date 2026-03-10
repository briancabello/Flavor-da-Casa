package nbcc.resto.repository;

import nbcc.resto.entity.SeatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatingJpaRepository extends JpaRepository<SeatingEntity, Long> {
    List<SeatingEntity> findByEventId(Long eventId);
}
