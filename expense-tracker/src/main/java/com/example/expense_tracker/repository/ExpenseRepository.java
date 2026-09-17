package com.example.expense_tracker.repository;

import com.example.expense_tracker.domain.entity.Expense;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

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
    SELECT AVG(cost)
    FROM Expense e INNER JOIN Category c
        ON e.category.id = c.id
    WHERE 
        c.id = :categoryId AND
        e.expenseDate >= :startDate AND
        e.expenseDate < :endDate
        
    GROUP BY c.id
    """)
    Float averageCurrentAmountByCategoryIdByDateBetween(Integer categoryId, LocalDate startDate, LocalDate endDate);


}
