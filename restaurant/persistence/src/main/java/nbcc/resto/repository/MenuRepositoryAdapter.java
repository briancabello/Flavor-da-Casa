package nbcc.resto.repository;

import nbcc.resto.dto.Menu;
import nbcc.resto.mapper.MenuMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class MenuRepositoryAdapter implements MenuRepository {

    private final MenuJpaRepository jpaRepository;
    private final MenuMapper mapper;

    public MenuRepositoryAdapter(MenuJpaRepository jpaRepository, MenuMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Collection<Menu> getAll() {
        var entities = jpaRepository.findAll();
        return mapper.toDTO(entities);
    }

    @Override
    public Optional<Menu> get(Long id) {
        var entity = jpaRepository.findById(id);
        return mapper.toDTO(entity);
    }

    @Override
    public Menu create(Menu menu) {
        var entity = mapper.toEntity(menu);
        entity = jpaRepository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public Menu update(Menu menu) {
        var entity = mapper.toEntity(menu);
        entity = jpaRepository.save(entity);
        return mapper.toDTO(entity);
    }

    @Override
    public void delete(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Collection<Menu> searchByName(String name) {
        var entities = jpaRepository.findByNameContainingIgnoreCase(name);
        return mapper.toDTO(entities);
    }
}
