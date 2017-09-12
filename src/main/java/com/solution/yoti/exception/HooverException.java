package com.solution.yoti.exception;

public class HooverException extends Exception {

    private String status;
    private String message;

    public HooverException(String status,Exception e){
        super(e);
        this.status = status;
        this.message = e.getMessage();
    }

    public String getStatus(){
        return status;
    }

    public String getMessage(){
        return message;
    }
}
