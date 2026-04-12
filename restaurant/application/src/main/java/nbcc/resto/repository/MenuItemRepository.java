package nbcc.resto.repository;

import nbcc.resto.dto.MenuItem;

import java.util.Collection;
import java.util.Optional;

public interface MenuItemRepository {

    Collection<MenuItem> getAll();

    Collection<MenuItem> getByMenuId(Long menuId);

    Optional<MenuItem> get(Long id);

    MenuItem create(MenuItem menuItem);

    MenuItem update(MenuItem menuItem);

    void delete(Long id);

    boolean exists(String name, Long menuId);
}
