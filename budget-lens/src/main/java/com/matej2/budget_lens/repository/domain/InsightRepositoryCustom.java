package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.domain.entity.Insight;

import java.util.List;

public interface InsightRepositoryCustom {
    void upsertAll(List<Insight> insights);
}
