package com.example.expense_tracker.repository;

import com.example.expense_tracker.domain.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    List<Expense>findByExpenseDateBetween(LocalDate startDate, LocalDate endDate);
}
