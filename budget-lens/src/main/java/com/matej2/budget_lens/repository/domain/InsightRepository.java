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

@Repository
public interface InsightRepository extends JpaRepository<Insight, Integer> {

    // TODO: Refactor to multi value insert
    @Transactional
    @Modifying
    @Query("""
    INSERT INTO Insight (yearMonth, category, standardDeviation, standardDeviationPercent, median, percentile90, isCostVariable, expenseCount, updatedAt)
    VALUES (:yearMonth, :category, :standardDeviation, :standardDeviationPercent, :median, :percentile90, :isCostVariable, :expenseCount, :updatedAt)
    ON CONFLICT (yearMonth, category)
    DO UPDATE SET
        standardDeviation = :standardDeviation,
        standardDeviationPercent = :standardDeviationPercent,
        median = :median,
        percentile90 = :percentile90,
        isCostVariable = :isCostVariable,
        expenseCount = :expenseCount,
        updatedAt = :updatedAt
    """)
    void updateByCategoryIdAndDate(
            @Param("standardDeviation") float standardDeviation,
            @Param("standardDeviationPercent") float standardDeviationPercent,
            @Param("category") Category category,
            @Param("yearMonth") YearMonth yearMonth,
            @Param("median") float median,
            @Param("percentile90") float percentile90,
            @Param("isCostVariable") boolean isCostVariable,
            @Param("expenseCount") Long expenseCount,
            @Param("updatedAt") LocalDate updatedAt
        );
}
