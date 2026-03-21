package nbcc.resto.repository;

import nbcc.resto.dto.MenuItem;

import java.util.Collection;
import java.util.Optional;

public interface MenuItemRepository {

    Collection<MenuItem> getByMenuId(Long menuId);

    Optional<MenuItem> get(Long id);

    MenuItem create(MenuItem menuItem);
}
