package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.domain.dto.CategoryResponse;
import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.mapper.CategoryMapper;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category getCategory(Integer id) {
        return categoryRepository.findById(id).orElseThrow();
    }
    public List<CategoryResponse> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }
}
