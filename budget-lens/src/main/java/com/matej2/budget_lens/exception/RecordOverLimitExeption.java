package com.matej2.budget_lens.exception;

public class RecordOverLimitExeption extends RuntimeException {
    public RecordOverLimitExeption(String message) {
        super(message);
    }
}
