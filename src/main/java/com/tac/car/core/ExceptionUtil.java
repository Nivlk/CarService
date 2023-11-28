package com.tac.car.core;



public class ExceptionUtil {
    private String message;

    public ExceptionUtil(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}