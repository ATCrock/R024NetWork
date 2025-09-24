package com.example.r024network.Result;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Data
@AllArgsConstructor // 全参数构造器
@NoArgsConstructor
public class AjaxResult<T>{
    public static final String SUCCESS_MESSAGE = "SUCCESS";
    public static final String FAILED_MESSAGE = "FAIL";
    public static final String SUCCESS_CONSERVE = "SUCCESS_CONSERVE";
    public static final String SUCCESS_DELETE = "SUCCESS_DELETE";
    public static final String SUCCESS_POST =  "SUCCESS_POST";



    public static final String FAILED_FOUND = "FAIL---The given userID does not exist in the database---"; // 404
    public static final String FAILED_QUERY = "FAIL---Unable to find the content corresponding to this ID---"; // 405
    public static final String FAILED_CREATE = "FAIL---This account has been registered---"; //403
    public static final String FAILED_POST = "FAIL---The content of the post cannot be empty---"; //410
    public static final String FAILED_LOGIN = "FAIL---The password corresponding to the account is incorrect---"; //410
    public static final String FAILED_NO_PERMISSION = "FAIL---Do not have permission to perform this operation---"; //420
    //public static final String FAILED_

    //    @JsonProperty("username")
//    private Integer username;
//    @JsonProperty("name")
//    private String name;
//    @Size(min=8, max = 16)
//    @JsonProperty("password")
//    private String password;
    private Integer code;
    private String message;
    private T user_data;
    private T user_data2;


    //private boolean isShowData;

    //private T data;
    //private T user_type;


    public static <N> AjaxResult<N> success() {
        return new AjaxResult<>(HttpStatus.OK.value(), SUCCESS_MESSAGE, null, null);
    }

    public static <N> AjaxResult<N> success(N user_data) {
        return new AjaxResult<>(HttpStatus.OK.value(), SUCCESS_MESSAGE, user_data, null);
    }

    public static <N> AjaxResult<N> success(N user_data1, N user_data2) {
        return new AjaxResult<>(HttpStatus.OK.value(), SUCCESS_MESSAGE, user_data1, user_data2);
    }

    public static <N> AjaxResult<N> fail(String errorMessage) {
        return new AjaxResult<>(HttpStatus.OK.value(), errorMessage, null, null);
    }
    public static <N> AjaxResult<N> fail(Integer statusCode, String errorMessage) {
        return new AjaxResult<>(statusCode, errorMessage, null, null);
    }
}
