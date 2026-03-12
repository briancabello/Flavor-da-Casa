package nbcc.resto.repository;

import nbcc.resto.entity.SeatingEntity;
import nbcc.resto.entity.SeatingTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatingTableJpaRepository extends JpaRepository<SeatingTableEntity, Long> {
    @Query("SELECT st.seating FROM SeatingTableEntity st WHERE st.diningTable.id = :tableId")
    List<SeatingEntity> findSeatingsByTableId(@Param("tableId") Long tableId);

    List<SeatingTableEntity> findBySeatingId(Long seatingId);

    void deleteByDiningTableId(@Param("diningTableId") Long diningTableId);
}
