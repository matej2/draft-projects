package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.model.entity.Category;
import com.matej2.budget_lens.model.entity.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

@Repository
public interface InsightRepository extends JpaRepository<Insight, Integer>, InsightRepositoryCustom  {
    List<Insight> findByYearMonthAndCategory(YearMonth yearMonth, Category category);
}
