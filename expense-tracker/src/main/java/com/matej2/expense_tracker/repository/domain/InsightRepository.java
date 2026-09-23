package com.matej2.expense_tracker.repository.domain;

import com.matej2.expense_tracker.domain.entity.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsightRepository extends JpaRepository<Insight, Integer> {

}
