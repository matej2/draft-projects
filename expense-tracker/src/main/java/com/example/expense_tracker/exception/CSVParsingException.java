package com.example.expense_tracker.exception;

public class CSVParsingException extends RuntimeException {
    public CSVParsingException(String message) {
        super(message);
    }
}
