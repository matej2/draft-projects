package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.domain.entity.RecordLimit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordLimiter  extends JpaRepository<RecordLimit, Integer> {
    public RecordLimit findOneByClassName(String className);
}
