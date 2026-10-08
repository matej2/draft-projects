package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.domain.entity.Category;
import com.matej2.budget_lens.domain.entity.Insight;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@Repository
public interface InsightRepository extends JpaRepository<Insight, Integer> {

    // TODO: Refactor to multi value insert
    @Transactional
    @Modifying
    @Query("""
    INSERT INTO Insight (yearMonth, category, standardDeviationPercent, median, percentile90, expenseCount, updatedAt, safeBudgetConfidence, budgetUtilization)
    VALUES (:yearMonth, :category, :standardDeviationPercent, :median, :percentile90, :expenseCount, :updatedAt, :safeBudgetConfidence, :budgetUtilization)
    ON CONFLICT (yearMonth, category)
    DO UPDATE SET
        standardDeviationPercent = :standardDeviationPercent,
        median = :median,
        percentile90 = :percentile90,
        expenseCount = :expenseCount,
        updatedAt = :updatedAt,
        safeBudgetConfidence = :safeBudgetConfidence,
        budgetUtilization = :budgetUtilization
    """)
    void updateByCategoryIdAndDate(
            @Param("standardDeviationPercent") Double standardDeviationPercent,
            @Param("category") Category category,
            @Param("yearMonth") YearMonth yearMonth,
            @Param("median") Double median,
            @Param("percentile90") Double percentile90,
            @Param("expenseCount") Long expenseCount,
            @Param("updatedAt") LocalDate updatedAt,
            @Param("safeBudgetConfidence") Double safeBudgetConfidence,
            @Param("budgetUtilization") Double budgetUtilization
        );

    public List<Insight> findByYearMonthAndCategory(YearMonth yearMonth, Category category);

}
