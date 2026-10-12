package com.matej2.budget_lens.utils;

import com.matej2.budget_lens.domain.dto.response.CategoryResponse;
import com.matej2.budget_lens.domain.dto.response.FrequencyResponse;

import java.util.List;
import java.util.Objects;

public class RecordUtils {
    public static CategoryResponse getCategoryById(List<CategoryResponse> categories, Integer categoryId) {
        return categories.stream()
                .filter(c -> Objects.equals(c.id(), categoryId))
                .findFirst().orElse(null);
    }

    public static FrequencyResponse getFrequencyById(List<FrequencyResponse> frequencies, Integer frequencyId) {
        return frequencies.stream()
                .filter(f -> Objects.equals(f.id(), frequencyId))
                .findFirst().orElse(null);
    }
}
