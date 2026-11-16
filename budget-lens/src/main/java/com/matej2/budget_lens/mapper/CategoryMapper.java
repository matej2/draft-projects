package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.model.entity.Category;
import com.matej2.budget_lens.model.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CategoryMapper  {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);


    Category categoryResponseToCategory(CategoryResponse categoryResponse);
    CategoryResponse categoryToCategoryResponse(Category category);
}
