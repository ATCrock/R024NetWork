package com.example.r024network.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class APIException extends RuntimeException{
    private int statusCode;
    private String errorMessage;


    public static APIException error(ExceptionEnum exceptionEnum) {
        APIException apiException = new APIException();
        apiException.setStatusCode(exceptionEnum.getCode());
        apiException.setErrorMessage(exceptionEnum.getResultMsg());
        return apiException;
    }


    public static APIException error(int code, String message) {
        APIException apiException = new APIException();
        apiException.setStatusCode(code);
        apiException.setErrorMessage(message);
        return apiException;
    }

    public static APIException error(String message) {
        APIException apiException = new APIException();
        apiException.setStatusCode(ExceptionEnum.UNKNOWN_ERROR.getCode());
        apiException.setErrorMessage(message);
        return apiException;
    }

    public static APIException success(Object data) {
        APIException apiException = new APIException();
        apiException.setStatusCode(200);
        apiException.setErrorMessage("null");
        return apiException;
    }


}
