package com.matej2.budget_lens.model.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;

@Getter
public class CSVImportRow {

        @CsvBindByName
        private String date;
        @CsvBindByName
        private int refNum;
        @CsvBindByName
        private String source;
        @CsvBindByName
        private String description;
        @CsvBindByName
        private Float positiveTraffic;
        @CsvBindByName
        private Float negativeTraffic;
}