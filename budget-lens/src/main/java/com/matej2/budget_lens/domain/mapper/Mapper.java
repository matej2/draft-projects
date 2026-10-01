package com.matej2.budget_lens.domain.mapper;

public interface Mapper<D, E> {
    E toEntity(D dto);
    D toDto(E entity);
}
