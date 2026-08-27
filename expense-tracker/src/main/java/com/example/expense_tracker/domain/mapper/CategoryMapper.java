package com.example.expense_tracker.domain.mapper;

import com.example.expense_tracker.domain.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper extends GenericMapper<Category, UserRequestDto, UserResponseDto> {
    // MapStruct automatically generates all implementation code here
}