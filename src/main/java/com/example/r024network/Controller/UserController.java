package com.example.r024network.Controller;

import com.example.r024network.dto.LoginRequest;
import com.example.r024network.dto.RegisterRequest;
import com.example.r024network.dto.UpadteInformationRequest;
import com.example.r024network.exception.APIException;
import com.example.r024network.result.AjaxResult;
import com.example.r024network.service.UserService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apifox/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

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
    public AjaxResult<LoginRequest> login(@Valid @RequestBody LoginRequest loginRequest){
        try {
            userService.login(loginRequest.getUserAccount(), loginRequest.getPassword());
        } catch (APIException e) {
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }

    @PostMapping("/update")
    public AjaxResult<UpadteInformationRequest> updateUserInformation(@Valid @RequestBody UpadteInformationRequest upadteInformationRequest) {
        try {
            userService.updateUserInformation(upadteInformationRequest.getPrevAccount(), upadteInformationRequest.getLatestUserAccount(),
                    upadteInformationRequest.getUserName(), upadteInformationRequest.getPassword());
        } catch (APIException e) {
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }
}
