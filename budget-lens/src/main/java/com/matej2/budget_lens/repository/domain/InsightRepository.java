package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.domain.entity.Insight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsightRepository extends JpaRepository<Insight, Integer> {

}
