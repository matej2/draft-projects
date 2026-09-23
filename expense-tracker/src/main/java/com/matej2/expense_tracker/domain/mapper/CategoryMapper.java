package com.matej2.expense_tracker.domain.mapper;

import com.matej2.expense_tracker.domain.dto.CategoryResponse;
import com.matej2.expense_tracker.domain.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
