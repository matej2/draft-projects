package com.example.expense_tracker.domain.mapper;

import java.util.List;

public interface GenericMapper<E, REQ, RES> {
    E toEntity(REQ requestDto);
    RES toResponseDto(E entity);
    List<E> toEntityList(List<REQ> requestDtoList);
    List<RES> toResponseDtoList(List<E> entityList);
}
