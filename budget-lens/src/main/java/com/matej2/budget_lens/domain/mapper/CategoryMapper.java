package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.response.CategoryResponse;
import com.matej2.budget_lens.domain.entity.Category;

public class CategoryMapper {
    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
