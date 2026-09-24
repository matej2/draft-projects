package com.matej2.budget_lens.repository.domain;

import com.matej2.budget_lens.domain.entity.Frequency;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FrequencyRepository  extends JpaRepository<Frequency, Integer> { }
