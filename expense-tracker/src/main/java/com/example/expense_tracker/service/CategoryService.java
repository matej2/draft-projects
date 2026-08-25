package com.example.expense_tracker.service;

import com.example.expense_tracker.domain.dto.CategoryResponse;
import com.example.expense_tracker.domain.entity.Category;
import com.example.expense_tracker.domain.mapper.CategoryMapper;
import com.example.expense_tracker.repository.CategoryRepository;
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
