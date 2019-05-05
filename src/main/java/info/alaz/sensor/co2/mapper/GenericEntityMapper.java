package info.alaz.sensor.co2.mapper;

import java.util.List;

public interface GenericEntityMapper<D, E> {
    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntityList(List<D> dtoList);

    List<D> toDtoList(List<E> entityList);
}