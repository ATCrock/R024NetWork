package com.example.r024network.Helper;

import com.example.r024network.Exception.APIException;

public class ResultHelper {
    public APIException failResult(Integer errCode, String errMsg){
        return new APIException(errCode, errMsg);
    }
}
