package nbcc.resto.repository;

import nbcc.resto.dto.MenuItem;
import nbcc.resto.mapper.MenuItemMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MenuItemRepositoryAdapter implements MenuItemRepository {

    private final MenuItemMapper mapper;
    private final MenuItemJpaRepository jpaRepository;

    public MenuItemRepositoryAdapter(MenuItemMapper mapper, MenuItemJpaRepository jpaRepository) {
        this.mapper = mapper;
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Collection<MenuItem> getByMenuId(Long menuId) {
        var entities = jpaRepository.findByMenuId(menuId);
        return mapper.toDTO(entities);
    }

    @Override
    public Optional<MenuItem> get(Long id) {
        var entity = jpaRepository.findById(id);
        return mapper.toDTO(entity);
    }

    @Override
    public MenuItem create(MenuItem menuItem) {
        var entity = mapper.toEntity(menuItem);
        entity = jpaRepository.save(entity);
        return mapper.toDTO(entity);
    }
}
