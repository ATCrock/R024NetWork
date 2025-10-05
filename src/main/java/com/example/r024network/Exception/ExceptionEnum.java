package com.example.r024network.Exception;

import lombok.Getter;

@Getter
public enum ExceptionEnum{
    SUCCESS(200, "成功"),
    ACCOUNT_REGISTER_ERROR(401, "该账号已注册"),
    LOGIN_ERROR(402 ,"账号或密码错误"),
    UNKNOWN_INFORMATION(410, "找不到对应信息"),
    NO_BLANK_ERROR(411, "信息不能为空"),
    ILLEGAL_PATH(412, "路径非法"),
    OUT_OF_MAX_FILES_ERROR(413, "文件数量过多"),
    OUTDATE_JWT(414, "jwt认证已过期"),
    UNKNOWN_JWT(415, "jwt验证失败"),
    UNSUPPORTED_TYPE(420, "不支持的格式"),
    SPRING_STARTED_ERROR(430, "spring容器初始化失败"),
    UNKNOWN_ERROR(500, "未知错误")
    ;

    private final int code;
    private final String resultMsg;
    ExceptionEnum(int Code, String resultMsg) {
        this.code = Code;
        this.resultMsg = resultMsg;
    }
}
