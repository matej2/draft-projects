package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.response.CategoryResponse;
import com.matej2.budget_lens.domain.entity.Category;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper implements Mapper<CategoryResponse, CategoryResponse, Category> {
    @Override
    public Category toEntity(CategoryResponse requestDto) {
        throw new NotImplementedException();
    }

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }
}
