package com.matej2.budget_lens.domain.mapper;

import com.matej2.budget_lens.domain.dto.response.FrequencyResponse;
import com.matej2.budget_lens.domain.entity.Frequency;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FrequencyMapper implements Mapper<FrequencyResponse, FrequencyResponse, Frequency> {
    private final ExpenseMapper expenseMapper;

    @Override
    public Frequency toEntity(FrequencyResponse requestDto) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public  FrequencyResponse toResponse(Frequency frequency) {

        return new FrequencyResponse(
                frequency.getId(),
                frequency.getNumber(),
                frequency.getDescription()
        );
    }
}
