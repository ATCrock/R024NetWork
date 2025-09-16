package com.example.r024network.service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.entity.Userdata;
import com.example.r024network.exception.APIException;
import com.example.r024network.service.UserService;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserdataMapper userdataMapper;

    @Override
    public void register(Integer userAccount, String userName, String password, Integer userType){
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
    public void login(Integer userAccount, String password){
        QueryWrapper<Userdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",password);
        Userdata userdata = userdataMapper.selectOne(queryWrapper);
        if (userdata==null){
            throw new APIException(402, "账号或密码错误");
        }
    }

    @Override
    public void updateUserInformation(Integer previousUserAccount, Integer newUserAccount, String newUserName, String newPassword){
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

}
