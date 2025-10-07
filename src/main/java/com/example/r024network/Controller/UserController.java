package com.example.r024network.Controller;

import com.example.r024network.dto.LoginRequest;
import com.example.r024network.dto.PullBorWListRequest;
import com.example.r024network.dto.RegisterRequest;
import com.example.r024network.dto.UpadteInformationRequest;
import com.example.r024network.Exception.APIException;
import com.example.r024network.Result.AjaxResult;
import com.example.r024network.Service.UserService;
import com.example.r024network.jwt.JWTTokenUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apifox/user")
@Slf4j
@CrossOrigin(origins = "*")

public class UserController {
    @Resource
    private UserService userService;

    /** 注册
     * @param registerRequest 注册需要的json流
     * @return {@link AjaxResult }
     */
    @PostMapping("/register")
    public AjaxResult<RegisterRequest> register(@Valid @RequestBody RegisterRequest registerRequest){

        if (userService.register(registerRequest.getUserAccount(), registerRequest.getUserName(), registerRequest.getPassword(), registerRequest.getUserType())){
            throw new APIException(200, "成功");
        }else {
            throw new APIException(401, "该用户已注册");
        }
    }

    /** 登录
     * @param loginRequest 登录需要的json流
     * 登录后会在user_data中返回jwt token，作为后续大部分接口验证的字段
     * @return {@link AjaxResult }>
     */
    @GetMapping("/login")
    public AjaxResult<String> login(@Valid @RequestBody LoginRequest loginRequest){
        String token;
        token = userService.login(loginRequest.getUserAccount(), loginRequest.getPassword());
        return AjaxResult.success(token);
    }

    /**更新个人信息
     * @param request 前后端网络请求
     * @return {@link AjaxResult }<{@link UpadteInformationRequest }>
     */
    @PutMapping("/update")
    public AjaxResult<UpadteInformationRequest> updateUserInformation(@Valid @RequestParam String new_account, String user_name, String password, HttpServletRequest request) {
        String userAccount = request.getAttribute("user_account").toString();
            userService.updateUserInformation(userAccount, new_account, user_name, password);
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
        userService.pullBlack(userAccount, pullBorWListRequest.getTargetAccount());
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
            userService.pullWhite(userAccount, pullBorWListRequest.getTargetAccount());
        return AjaxResult.success();
    }

    @GetMapping
    public AjaxResult<String> loginRefresh(@Valid @RequestBody HttpServletRequest request){
        String userAccount = request.getAttribute("user_account").toString();
        int userId = (int) request.getAttribute("user_id");
        String token;
            token = JWTTokenUtil.generateToken(userAccount, userId);
        return AjaxResult.success(token);
    }
}
