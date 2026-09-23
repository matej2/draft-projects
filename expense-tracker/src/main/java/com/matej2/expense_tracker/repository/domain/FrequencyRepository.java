package com.matej2.expense_tracker.repository.domain;

import com.matej2.expense_tracker.domain.entity.Frequency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrequencyRepository  extends JpaRepository<Frequency, Integer> { }
