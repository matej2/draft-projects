package com.matej2.budget_lens.service.domain;

import com.matej2.budget_lens.mapper.CategoryMapper;
import com.matej2.budget_lens.model.entity.Category;
import com.matej2.budget_lens.model.response.CategoryResponse;
import com.matej2.budget_lens.repository.domain.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper = CategoryMapper.INSTANCE;

    public CategoryResponse getCategory(Integer id) {
        return categoryMapper.categoryToCategoryResponse(
                categoryRepository.findById(id).orElseThrow()
        );
    }
    public List<CategoryResponse> getAllCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        return allCategories.stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .toList();
    }
    public Category getUncategorizedCategory() {
        return categoryRepository.findByName("Uncategorized");
    }
}
