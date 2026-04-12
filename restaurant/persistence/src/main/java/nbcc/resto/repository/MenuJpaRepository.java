package nbcc.resto.repository;

import nbcc.resto.entity.MenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuJpaRepository extends JpaRepository<MenuEntity, Long> {
    List<MenuEntity> findByNameContainingIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
