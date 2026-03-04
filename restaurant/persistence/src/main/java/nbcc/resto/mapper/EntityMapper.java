package nbcc.resto.mapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface EntityMapper<D, E> {

    E toEntity(D domain);
    D toDomain(E entity);

    default Collection<E> toEntity(Collection<D> domainList){
        if (domainList == null) return List.of();
        return domainList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    default Collection<D> toDomain(Collection<E> entityList){
        if (entityList == null) return List.of();
        return entityList.stream().map(this::toDomain).collect(Collectors.toList());
    }
}
