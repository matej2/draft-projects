package com.matej2.budget_lens.utils;

import com.opencsv.CSVReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CsvUtils {
    public static final String CSV_TYPE = "text/csv";

    public static CSVReader getCsvReader(InputStream input) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));
        return new CSVReader(reader);
    }

    public static boolean hasCSVFormat(MultipartFile file) {

        return CSV_TYPE.equals(file.getContentType());
    }

}
