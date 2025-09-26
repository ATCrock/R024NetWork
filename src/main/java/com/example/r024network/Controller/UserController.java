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

//    @GetMapping("login_refresh")
//    public AjaxResult<String> loginByToken(HttpServletRequest request, HttpServletResponse response){
//        try {
//            jwtRequestFilter.doFilterInternal(request, response);
//        } catch (APIException e) {
//            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
//        } catch (ServletException | IOException e) {
//            throw new RuntimeException(e);
//        }
//        return AjaxResult.success();
//    }

    @PutMapping("/update")
    public AjaxResult<UpadteInformationRequest> updateUserInformation(@Valid @RequestBody UpadteInformationRequest upadteInformationRequest) {
        try {
            userService.updateUserInformation(upadteInformationRequest.getPrevAccount(), upadteInformationRequest.getLatestUserAccount(),
                    upadteInformationRequest.getUserName(), upadteInformationRequest.getPassword());
        } catch (APIException e) {
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }

    @PostMapping("/pull_black")
    public AjaxResult<PullBorWListRequest> pullBlack(@Valid @RequestBody PullBorWListRequest pullBorWListRequest){
        try{
            userService.pullBlack(pullBorWListRequest.getUserAccount(), pullBorWListRequest.getTargetAccount());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }

    @PostMapping("/pull_white")
    public AjaxResult<PullBorWListRequest> pullWhite(@Valid @RequestBody PullBorWListRequest pullBorWListRequest){
        try{
            userService.pullWhite(pullBorWListRequest.getUserAccount(), pullBorWListRequest.getTargetAccount());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }

//    @PostMapping("/update_pic")
//    public AjaxResult<>
}
