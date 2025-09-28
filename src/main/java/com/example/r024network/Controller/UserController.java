package com.example.r024network.Controller;

import com.example.r024network.Filter.JWTRequestFilter;
import com.example.r024network.dto.LoginRequest;
import com.example.r024network.dto.PullBorWListRequest;
import com.example.r024network.dto.RegisterRequest;
import com.example.r024network.dto.UpadteInformationRequest;
import com.example.r024network.Exception.APIException;
import com.example.r024network.Result.AjaxResult;
import com.example.r024network.Service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/apifox/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    /** 注册
     * @param registerRequest 注册需要的json流
     * @return {@link AjaxResult }
     */
    @PostMapping("/register")
    public AjaxResult<RegisterRequest> register(@Valid @RequestBody RegisterRequest registerRequest){
        try {
            userService.register(registerRequest.getUserAccount(), registerRequest.getUserName(), registerRequest.getPassword(), registerRequest.getUserType());
        } catch (APIException e) {
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }


    /** 登录
     * @param loginRequest 登录需要的json流
     * 登录后会在user_data中返回jwt token，作为后续大部分接口验证的字段
     * @return {@link AjaxResult }>
     */
    @GetMapping("/login")
    public AjaxResult<String> login(@Valid @RequestBody LoginRequest loginRequest){
        String token;
        try {
            token = userService.login(loginRequest.getUserAccount(), loginRequest.getPassword());
        } catch (APIException e) {
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success(token);
    }


    /**更新个人信息
     * @param upadteInformationRequest 更新个人信息需要的json流
     * @param request 前后端网络请求
     * @return {@link AjaxResult }<{@link UpadteInformationRequest }>
     */
    @PutMapping("/update")
    public AjaxResult<UpadteInformationRequest> updateUserInformation(@Valid @RequestBody UpadteInformationRequest upadteInformationRequest, HttpServletRequest request) {
        String userAccount = (String) request.getAttribute("user_account");
        try {
            userService.updateUserInformation(userAccount, upadteInformationRequest.getLatestUserAccount(),
                    upadteInformationRequest.getUserName(), upadteInformationRequest.getPassword());
        } catch (APIException e) {
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }


    /** 拉黑
     * @param pullBorWListRequest 拉黑需要的json流(和取消拉黑共用
     * @param request 前后端网络请求
     * @return {@link AjaxResult }<{@link PullBorWListRequest }>
     */
    @PostMapping("/pull_black")
    public AjaxResult<PullBorWListRequest> pullBlack(@Valid @RequestBody PullBorWListRequest pullBorWListRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
        try{
            userService.pullBlack(userAccount, pullBorWListRequest.getTargetAccount());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }

    /** 取消拉黑
     * @param pullBorWListRequest 取消拉黑需要的json流(和拉黑共用
     * @param request 前后端网络请求
     * @return {@link AjaxResult }
     */
    @PostMapping("/pull_white")
    public AjaxResult<PullBorWListRequest> pullWhite(@Valid @RequestBody PullBorWListRequest pullBorWListRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
        try{
            userService.pullWhite(userAccount, pullBorWListRequest.getTargetAccount());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }
}
