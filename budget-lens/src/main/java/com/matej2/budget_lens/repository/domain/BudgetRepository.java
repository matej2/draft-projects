package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.domain.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Integer> {
    List<Budget> findAllByCategoryId(Integer categoryId);

}
