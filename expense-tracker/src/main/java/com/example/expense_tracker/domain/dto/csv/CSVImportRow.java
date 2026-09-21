package com.example.expense_tracker.domain.dto.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;

@Getter
public class CSVImportRow extends CsvBean {

        @CsvBindByName
        private String date;

        // Automatically infer column name as 'Age'
        @CsvBindByName
        private int refNum;

        // getters and setters
}