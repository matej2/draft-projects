package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.config.annotation.RecordLimitEnabled;
import com.matej2.budget_lens.domain.entity.Expense;
import com.matej2.budget_lens.domain.jpa.CategoryInsightResultRow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@RecordLimitEnabled(entityName = "Expense")
@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    @Query(value = """
    SELECT SUM(cost)
    FROM Expense e INNER JOIN Category c
        ON e.category.id = c.id
    WHERE
        c.id = :categoryId AND
        e.expenseDate >= :startDate AND
        e.expenseDate < :endDate
    GROUP BY c.id
    """)
    Float summarizeCurrentAmountByCategoryIdByDateBetween(Integer categoryId, LocalDate startDate, LocalDate endDate);
    @Query(value = """
    SELECT NEW com.matej2.budget_lens.domain.jpa.CategoryInsightResultRow(
        stddev_pop(e.cost) as std_dev,
        ((stddev_pop(e.cost) / SQRT(COUNT(e.id))) / AVG(e.cost)) * 100 as stddev_percent,
        AVG(e.cost) as avg_cost,
        percentile_cont(0.5) WITHIN GROUP (
            ORDER BY
                e.cost
        ) as p50,
        percentile_cont(0.75) WITHIN GROUP (
            ORDER BY
                e.cost
        ) as p90,
        (((stddev_pop(e.cost) / SQRT(COUNT(e.id))) / AVG(e.cost)) * 100 < 10) as is_fixed_cost,
        COUNT(e.id) as expenseCount
    ) FROM Expense e INNER JOIN Category c
            ON e.category.id = c.id
        WHERE
            c.id = :categoryId AND
            e.expenseDate >= :startDate AND
            e.expenseDate < :endDate
        GROUP BY c.id
    """)
    CategoryInsightResultRow insightsByCategoryIdByDateBetween(
            @Param("categoryId") Integer categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
