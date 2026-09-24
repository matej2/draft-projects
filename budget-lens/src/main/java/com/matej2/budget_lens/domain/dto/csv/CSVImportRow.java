package com.matej2.budget_lens.domain.dto.csv;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;

@Getter
public class CSVImportRow extends CsvBean {

        @CsvBindByName
        private String date;
        @CsvBindByName
        private int refNum;
        @CsvBindByName
        private String subject;
        @CsvBindByName
        private String description;
        @CsvBindByName
        private Float positiveTraffic;
        @CsvBindByName
        private Float negativeTraffic;
}