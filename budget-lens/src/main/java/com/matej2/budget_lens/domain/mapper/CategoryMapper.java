package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.response.CategoryResponse;
import com.matej2.budget_lens.domain.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements Mapper<CategoryResponse, CategoryResponse, Category> {
    @Override
    public Category toEntity(CategoryResponse requestDto) {
        Category category = new Category();
        category.setId(requestDto.id());
        category.setName(requestDto.name());

        return category;
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
