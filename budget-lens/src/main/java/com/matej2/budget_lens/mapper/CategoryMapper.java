package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.model.response.CategoryResponse;
import com.matej2.budget_lens.model.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements Mapper<CategoryResponse, CategoryResponse, Category> {
    @Override
    public Category toEntity(CategoryResponse requestDto) {
        Category category = new Category();
        category.setName(requestDto.name());
        category.setId(requestDto.id());

        return category;
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
