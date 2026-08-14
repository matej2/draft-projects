package com.example.expense_tracker.repository;

import com.example.expense_tracker.domain.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> { }
