package com.alekseyz.testtask.springsecurityjwt.exceptionhandling;

public class TokenException extends RuntimeException{
    public TokenException(String message) {
        super(message);
    }
}
