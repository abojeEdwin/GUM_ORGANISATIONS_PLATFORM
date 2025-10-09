package com.EnumDayTask.exception;

public class LIMIT_EXCEEDED extends RuntimeException {
    public LIMIT_EXCEEDED(String message) {
        super(message);
    }
}
