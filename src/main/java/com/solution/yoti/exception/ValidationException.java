package com.solution.yoti.exception;

public class ValidationException extends Exception {

    private String message;

    public ValidationException(String message){
        super(message);
        this.message = message;
    }

    public String getMessage(){
        return message;
    }
}
