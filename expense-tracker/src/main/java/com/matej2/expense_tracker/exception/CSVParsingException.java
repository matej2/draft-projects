package com.matej2.expense_tracker.exception;

public class CSVParsingException extends RuntimeException {
    public CSVParsingException(String message) {
        super(message);
    }
}
