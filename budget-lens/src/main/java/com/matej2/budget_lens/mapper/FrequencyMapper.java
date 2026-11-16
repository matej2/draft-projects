package com.matej2.budget_lens.mapper;

import com.matej2.budget_lens.model.entity.Frequency;
import com.matej2.budget_lens.model.response.FrequencyResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FrequencyMapper {
    FrequencyMapper INSTANCE = Mappers.getMapper(FrequencyMapper.class);

    Frequency frequencyResponseToFrequency(FrequencyResponse frequencyResponse);
    FrequencyResponse frequencyToFrequencyResponse(Frequency frequency);
}
