package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.Filter.JWTRequestFilter;
import com.example.r024network.Helper.StringSplitter;
import com.example.r024network.entity.Userdata;
import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.UserService;
import com.example.r024network.jwt.JWTTokenUtil;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserdataMapper userdataMapper;
    private final StringSplitter stringSplitter = new StringSplitter();
    private final WrapperHelper  wrapperHelper = new WrapperHelper();
    private JWTRequestFilter jwtRequestFilter;

    @Override
    public void register(String userAccount, String userName, String password, Integer userType){
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account",userAccount));
        if (userdata==null){
            userdata = Userdata.builder().userAccount(userAccount).userName(userName).userPassword(password).userType(userType).userHeadPortraitAddress("default address").build();
            userdataMapper.insert(userdata);
        }
        else {
            throw new APIException(401, "该账号已注册");
        }
    }

    @Override
    public String login(String userAccount, String password){
        QueryWrapper<Userdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",password); // 检索用户密码同时对应的用户
        Userdata userdata = userdataMapper.selectOne(queryWrapper);
        if (userdata==null){
            throw new APIException(402, "账号或密码错误");
        }else  {
            return JWTTokenUtil.generateToken(userAccount, userdata.getUserId());
        }
    }

    @Override
    public void updateUserInformation(String previousUserAccount, String newUserAccount, String newUserName, String newPassword){

        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", previousUserAccount));
        if (userdata==null){
            throw new APIException(410, "未找到该账号");
        }
        else{
            if (newUserName != null && !newUserName.isBlank()) { // 如果更新后的用户名不是空的或者不存在 就更新名字
                userdata.setUserName(newUserName);
            }else userdata.setUserName(userdata.getUserName());
            if (newUserAccount != null) { // 如果更新后的用户账号不是空的就更新
                userdata.setUserAccount(newUserAccount);
            }else userdata.setUserAccount(previousUserAccount);
            if (newPassword != null && !newPassword.isBlank()) { // 如果更新后的密码不是空的或者不存在 就更新
                userdata.setUserPassword(newPassword);
            }else userdata.setUserPassword(userdata.getUserPassword());
            userdataMapper.insertOrUpdate(userdata);
        }

    }

    @Override
    public void pullBlack(String userAccount, String targetAccount){
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", userAccount));
        // 获取userData
        Userdata targetdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", targetAccount));
        // 获取targetData
        if (targetdata==null){
            throw new APIException(410, "未找到需要被拉黑的账号");
        }
        // 不做user处理是因为可以读取到userAccount
        else{  // 获取目标账号id,检测用户账号黑名单中是否存在该id，若不存在则拉黑
            userdata.setBlackList(userdata.getBlackList() + ";" + targetdata.getUserId());
            userdataMapper.insertOrUpdate(userdata);
        }
    }

    @Override
    public void pullWhite(String userAccount, String targetAccount){
        // 获取用户账号与目标账号，如果存在进行下一步
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", userAccount));
        Userdata targetdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", targetAccount));
        if (targetdata==null){
            throw new APIException(410, "未找到需要解除拉黑的账号");
        }
        else { // 获取目标账号id,检测用户账号黑名单中是否存在该id，若存在则取消拉黑
            Integer targetUserId = targetdata.getUserId();
            String staticBlackList = userdata.getBlackList();
            int[] intBlackList = stringSplitter.splitToIntArray(staticBlackList);
            intBlackList = Arrays.stream(intBlackList).filter(item -> item != targetUserId).toArray();
            String stringBlackList = stringSplitter.joinToString(intBlackList);
            userdata.setBlackList(stringBlackList);
            userdataMapper.insertOrUpdate(userdata);
        }
    }

    @Override
    public int[] getBlack(String userAccount){
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", userAccount));
        return stringSplitter.splitToIntArray(userdata.getBlackList());
    }

//    @Override
//    public void loginByToken(String token) {
//        jwtRequestFilter.doFilterInternal();
//    }

}
