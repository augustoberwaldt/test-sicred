package com.sicredi.app.exception;


public class BusinessException extends RuntimeException {
    public BusinessException(String errorMessage) {
        super(errorMessage);
    }
}