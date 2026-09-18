package com.example.expense_tracker.repository;

import com.example.expense_tracker.domain.entity.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsightRepository extends JpaRepository<Insight, Integer> {

}
