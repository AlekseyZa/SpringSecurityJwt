package com.alekseyz.testtask.springsecurityjwt.exceptionhandling;

public class UserException extends RuntimeException{
    public UserException(String message) {
        super(message);
    }
}
