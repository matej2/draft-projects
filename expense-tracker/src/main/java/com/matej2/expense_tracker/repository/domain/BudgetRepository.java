package com.matej2.expense_tracker.repository.domain;

import com.matej2.expense_tracker.domain.entity.Budget;
import com.matej2.expense_tracker.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    Budget findOneByCategory(Category category);

}
