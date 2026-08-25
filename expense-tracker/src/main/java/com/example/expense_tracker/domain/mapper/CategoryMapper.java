package com.example.expense_tracker.domain.mapper;

import com.example.expense_tracker.domain.dto.CategoryResponse;
import com.example.expense_tracker.domain.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public static CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getName());
    }
}
