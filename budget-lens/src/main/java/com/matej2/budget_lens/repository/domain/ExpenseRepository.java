package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.config.annotation.RecordLimitEnabled;
import com.matej2.budget_lens.model.entity.Expense;
import com.matej2.budget_lens.model.jpa.CategoryInsightResultRow;
import org.springframework.data.domain.Page;
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
    List<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);
    Page<Expense> findByExpenseDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query(value = """
    SELECT NEW com.matej2.budget_lens.model.jpa.CategoryInsightResultRow(
        (stddev_pop(e.cost) /  AVG(e.cost)),
        AVG(e.cost),
        percentile_cont(0.5) WITHIN GROUP (
            ORDER BY
                e.cost
        ),
        percentile_cont(0.75) WITHIN GROUP (
            ORDER BY
                e.cost
        ),
        COUNT(e.id)
    ) FROM Expense e INNER JOIN Category c
            ON e.category.id = c.id
        WHERE
            c.id = :categoryId AND
            e.expenseDate >= :startDate AND
            e.expenseDate < :endDate
        GROUP BY c.id
    """)
    CategoryInsightResultRow getInsightsByCategoryIdByDateBetween(
            @Param("categoryId") Integer categoryId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
