package com.example.expense_tracker.repository;

import com.example.expense_tracker.domain.entity.Frequency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FrequencyRepository  extends JpaRepository<Frequency, Integer> {
    List<Frequency> findByNumber(short number);
}
