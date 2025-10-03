package com.example.r024network.Result;

import lombok.*;
import org.springframework.http.HttpStatus;


/**
 *一个全局错误处理类
 * 错误代码如下<br>
 * 200 success<br>
 * 401 该账号已注册<br>
 * 402 账号或密码错误<br>
 * 410 找不到对应信息（拉黑，取消拉黑，查询账号对应内容等）<br>
 * 411 信息不能为空（一般是发帖子的报错类型）<br>
 * 412 路径非法<br>
 * 413 文件数量过多<br>
 * 414 jwt验证已过期<br>
 * 415 jwt验证失败(解析)<br>
 * 420 不支持的格式（信息输入格式有误或不在规定范围内）<br>
 * 430 spring容器初始化失败（定时任务相关内容报错）<br>
 * 454 泛用报错，捕捉可能遇到的问题
 */
@Getter
@Setter
@Data
@AllArgsConstructor // 全参数构造器
@NoArgsConstructor

public class AjaxResult<T>{
    public static final String SUCCESS_MESSAGE = "SUCCESS";
    private Integer code;
    private String message;
    private T user_data;
    private T user_data2;

    public static <N> AjaxResult<N> success() {
        return new AjaxResult<>(HttpStatus.OK.value(), SUCCESS_MESSAGE, null, null);
    }

    public static <N> AjaxResult<N> success(N user_data) {
        return new AjaxResult<>(HttpStatus.OK.value(), SUCCESS_MESSAGE, user_data, null);
    }

    public static <N> AjaxResult<N> success(N user_data1, N user_data2) {
        // userData1是帖子，userData2是评论
        return new AjaxResult<>(HttpStatus.OK.value(), SUCCESS_MESSAGE, user_data1, user_data2);
    }

    public static <N> AjaxResult<N> fail(Integer statusCode, String errorMessage) {
        return new AjaxResult<>(statusCode, errorMessage, null, null);
    }
}
