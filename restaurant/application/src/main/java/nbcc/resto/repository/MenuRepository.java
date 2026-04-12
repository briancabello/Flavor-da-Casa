package nbcc.resto.repository;

import nbcc.resto.dto.Menu;

import java.util.Collection;
import java.util.Optional;

public interface MenuRepository {

    Collection<Menu> getAll();

    Optional<Menu> get(Long id);

    Menu create(Menu menu);

    Menu update(Menu menu);

    void delete(Long id);

    Collection<Menu> searchByName(String name);

    boolean exists(String name);
}
