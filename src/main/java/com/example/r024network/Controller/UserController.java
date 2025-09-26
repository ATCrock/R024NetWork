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
    @Resource
    private JWTRequestFilter jwtRequestFilter;

    @PostMapping("/register")
    public AjaxResult<RegisterRequest> register(@Valid @RequestBody RegisterRequest registerRequest){
        try {
            userService.register(registerRequest.getUserAccount(), registerRequest.getUserName(), registerRequest.getPassword(), registerRequest.getUserType());
        } catch (APIException e) {
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }


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
