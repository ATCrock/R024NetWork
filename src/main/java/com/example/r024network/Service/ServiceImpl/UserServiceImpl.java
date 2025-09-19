package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.StringHelper.StringSplitter;
import com.example.r024network.entity.Userdata;
import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.UserService;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserdataMapper userdataMapper;
    private StringSplitter stringSplitter = new StringSplitter();
    @Override
    public void register(String userAccount, String userName, String password, Integer userType){
        QueryWrapper<Userdata>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        Userdata userdata = userdataMapper.selectOne(queryWrapper);
        if (userdata==null){
            userdata = Userdata.builder().userAccount(userAccount).userName(userName).userPassword(password).userType(userType).userHeadPortraitAddress("default address").build();
            userdataMapper.insert(userdata);
        }
        else {
            throw new APIException(401, "该账号已注册");
        }
    }

    @Override
    public void login(String userAccount, String password){
        QueryWrapper<Userdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",password);
        Userdata userdata = userdataMapper.selectOne(queryWrapper);
        if (userdata==null){
            throw new APIException(402, "账号或密码错误");
        }
    }

    @Override
    public void updateUserInformation(String previousUserAccount, String newUserAccount, String newUserName, String newPassword){
        QueryWrapper<Userdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",previousUserAccount);
        Userdata userdata = userdataMapper.selectOne(queryWrapper);
        if (userdata==null){
            throw new APIException(410, "未找到该账号");
        }
        else{
            if (newUserName != null && !newUserName.isBlank()) {
                userdata.setUserName(newUserName);
            }else userdata.setUserName(userdata.getUserName());
            if (newUserAccount != null) {
                userdata.setUserAccount(newUserAccount);
            }else userdata.setUserAccount(previousUserAccount);
            if (newPassword != null && !newPassword.isBlank()) {
                userdata.setUserPassword(newPassword);
            }else userdata.setUserPassword(userdata.getUserPassword());
            userdataMapper.insertOrUpdate(userdata);
        }

    }

    @Override
    public void pullBlack(String userAccount, String targetAccount){
        QueryWrapper<Userdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        Userdata userdata = userdataMapper.selectOne(queryWrapper);
        // 获取userData
        QueryWrapper<Userdata> targetQueryWrapper = new QueryWrapper<>();
        targetQueryWrapper.eq("user_account",targetAccount);
        Userdata targetdata = userdataMapper.selectOne(targetQueryWrapper);
        // 获取targetData
        if (targetdata==null){
            throw new APIException(410, "未找到需要被拉黑的账号");
        }
        // 不做user处理是因为jwt可以读取到userAccount
        else{
            String staticBlackList = userdata.getBlackList();
            Integer blackUserId = targetdata.getUserId();
            userdata.setBlackList(staticBlackList + ";" + blackUserId);
            userdataMapper.insertOrUpdate(userdata);
        }
    }

    @Override
    public void pullWhite(String userAccount, String targetAccount){
        QueryWrapper<Userdata> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account",userAccount);
        Userdata userdata = userdataMapper.selectOne(userQueryWrapper);
        QueryWrapper<Userdata> targetQueryWrapper = new QueryWrapper<>();
        targetQueryWrapper.eq("user_account",targetAccount);
        Userdata targetdata = userdataMapper.selectOne(targetQueryWrapper);
        if (targetdata==null){
            throw new APIException(410, "未找到需要解除拉黑的账号");
        }
        else {
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
        QueryWrapper<Userdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        Userdata userdata = userdataMapper.selectOne(queryWrapper);
        return stringSplitter.splitToIntArray(userdata.getBlackList());
    }

}
