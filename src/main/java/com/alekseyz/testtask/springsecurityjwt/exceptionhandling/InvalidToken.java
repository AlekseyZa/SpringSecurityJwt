package com.alekseyz.testtask.springsecurityjwt.exceptionhandling;

public class InvalidToken extends RuntimeException{
    public InvalidToken(String message) {
        super(message);
    }
}
