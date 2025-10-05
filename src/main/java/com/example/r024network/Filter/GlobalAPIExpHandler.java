package com.example.r024network.Filter;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Exception.ExceptionEnum;
import com.example.r024network.Result.AjaxResult;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalAPIExpHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalAPIExpHandler.class);

    @ExceptionHandler(value = APIException.class)
    @ResponseBody
    public AjaxResult<Object> handleException(HttpServletRequest request, APIException e) {
        return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
    }

    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public AjaxResult<Object> exceptionHandler(HttpServletRequest request, NullPointerException e){
        logger.error("发生空指针异常！原因是:",e);
        return AjaxResult.fail(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public AjaxResult<Object> exceptionHandler(HttpServletRequest request, Exception e) {
        logger.error("未知错误",e);
        return AjaxResult.fail(ExceptionEnum.UNKNOWN_ERROR, e.getMessage());
    }
}
