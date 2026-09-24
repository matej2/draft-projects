package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.domain.entity.Budget;
import com.matej2.budget_lens.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    Budget findOneByCategory(Category category);

}
