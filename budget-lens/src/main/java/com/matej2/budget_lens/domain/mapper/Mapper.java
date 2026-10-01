package com.matej2.budget_lens.domain.mapper;

public interface Mapper<I, O, E> {
    E toEntity(I requestDto);
    O toResponse(E entity);
}
