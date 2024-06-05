package com.alekseyz.testtask.springsecurityjwt.exceptionhandling;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(String message) {
        super(message);
    }
}
