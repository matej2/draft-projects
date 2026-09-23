package com.matej2.expense_tracker.utils;

import org.springframework.web.multipart.MultipartFile;

public class CSVHelper {
    public static final String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }
}
