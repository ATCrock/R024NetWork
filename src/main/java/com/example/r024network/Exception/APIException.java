package com.example.r024network.Exception;

import lombok.Getter;

@Getter
public class APIException extends RuntimeException{
    private final int statusCode;
    private final String errorMessage;

    public APIException(int statusCode, String errorMessage) {
        super(errorMessage);
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public APIException(int statusCode, String errorMessage, Throwable cause) {
        super(errorMessage, cause);
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }
}
