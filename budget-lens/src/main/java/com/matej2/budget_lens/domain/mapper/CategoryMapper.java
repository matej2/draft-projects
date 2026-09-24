package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.CategoryResponse;
import com.matej2.budget_lens.domain.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
