package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.Helper.StringSplitter;
import com.example.r024network.entity.Userdata;
import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.UserService;
import com.example.r024network.jwt.JWTTokenUtil;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserdataMapper userdataMapper;
    private final StringSplitter stringSplitter = new StringSplitter();
    private final WrapperHelper  wrapperHelper = new WrapperHelper();

    @Override
    public boolean register(String userAccount, String userName, String password, Integer userType){
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account",userAccount));
        if (userdata==null){
            userdata = Userdata.builder().userAccount(userAccount).userName(userName).userPassword(password).userType(userType).userHeadPortraitId("default address").build();
            userdataMapper.insert(userdata);
            return true;
        }
        return false;
    }



    @Override
    public String login(String userAccount, String password){
        QueryWrapper<Userdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",userAccount);
        queryWrapper.eq("user_password",password); // 检索用户密码同时对应用户账号
        Userdata userdata = userdataMapper.selectOne(queryWrapper);
        if (userdata!=null) {
            return JWTTokenUtil.generateToken(userAccount, userdata.getUserId());// 登录成功后会返回jwtToken，作为登录凭据
        }
        return null;
    }

    @Override
    public void updateUserInformation(String previousUserAccount, String newUserAccount, String newUserName, String newPassword){
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", previousUserAccount));
        if (userdata==null){
            throw new APIException(410, "未找到该账号");
        }
        else{
            if (newUserName != null && !newUserName.isBlank()) { // 如果输入的想要更新的用户名不是空的或者不存在 就更新名字
                userdata.setUserName(newUserName);
            }else userdata.setUserName(userdata.getUserName());

            if (newUserAccount != null) { // 如果更新后的用户账号不是空的 就更新~
                userdata.setUserAccount(newUserAccount);
            }else userdata.setUserAccount(previousUserAccount);

            if (newPassword != null && !newPassword.isBlank()) { // 如果更新后的密码不是空的或者不存在 就更新~
                userdata.setUserPassword(newPassword);
            }else userdata.setUserPassword(userdata.getUserPassword());
            userdataMapper.insertOrUpdate(userdata);
        }

    }

    @Override
    public void pullBlack(Integer userAccount, Integer targetAccount){
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", userAccount));
        // 获取userData
        Userdata targetdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", targetAccount));
        // 获取targetData
        if (targetdata==null){
            throw new APIException(410, "未找到需要被拉黑的账号");
        }
        // 不做user处理是因为可以读取到userAccount
        else{  // 获取目标账号id,检测用户账号黑名单中是否存在该id，若不存在则拉黑
            userdata.setBlackList(userdata.getBlackList() + ";" + targetdata.getUserId());// 黑名单用数字+分号存在数据库内，取出时去掉分号并存入数组
            userdataMapper.insertOrUpdate(userdata);
        }
    }

    @Override
    public void pullWhite(Integer userAccount, Integer targetAccount){
        // 获取用户账号与目标账号，如果存在进行下一步
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", userAccount));
        Userdata targetdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", targetAccount));
        if (targetdata==null){
            throw new APIException(410, "未找到需要解除拉黑的账号");
        }
        else {
            // 获取目标账号id,检测用户账号黑名单中是否存在该id，若存在则取消拉黑
            Integer targetUserId = targetdata.getUserId();
            int[] intBlackList = stringSplitter.splitToIntArray(userdata.getBlackList());
            // 把除了拉白的用户id之外的其他黑名单用户重新塞回去
            intBlackList = Arrays.stream(intBlackList).filter(item -> item != targetUserId).toArray();
            // 再变成String
            String stringBlackList = stringSplitter.joinToString(intBlackList);
            userdata.setBlackList(stringBlackList);
            userdataMapper.insertOrUpdate(userdata);
        }
    }

    @Override
    public int[] getBlack(Integer userAccount){
        // 获取当前用户黑名单
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", userAccount));
        return stringSplitter.splitToIntArray(userdata.getBlackList());
    }

    @Override
    public String loginRefresh(String userAccount, int userId){
        return JWTTokenUtil.generateToken(userAccount, userId);
    }
}
