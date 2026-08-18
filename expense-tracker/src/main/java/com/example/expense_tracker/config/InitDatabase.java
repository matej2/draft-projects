package com.example.expense_tracker.config;

import com.example.expense_tracker.domain.entity.Frequency;
import com.example.expense_tracker.repository.FrequencyRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component

public class InitDatabase implements CommandLineRunner {

    private final FrequencyRepository myRepository;

    public InitDatabase(FrequencyRepository myRepository) {
        this.myRepository = myRepository;
    }

    // Usually we would load data manually,
    // but this data is a business critical
    @Override
    @Async
    @Transactional
    public void run(String... args) {
/*
        List<Frequency> frequencyList =  new ArrayList<>();
        if (this.myRepository.findByNumber((short) 1).isEmpty())
            frequencyList.add(new Frequency((short) 1, "Yearly"));
        if (this.myRepository.findByNumber((short)12).isEmpty())
            frequencyList.add(new Frequency((short) 12, "Monthly"));
        if (this.myRepository.findByNumber((short)52).isEmpty())
            frequencyList.add(new Frequency((short) 52, "Weekly"));
        if (this.myRepository.findByNumber((short)365).isEmpty())
            frequencyList.add(new Frequency((short) 365, "Daily"));

        this.myRepository.saveAll(frequencyList);
*/
    }
}