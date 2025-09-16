package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.Service.PostService;
import com.example.r024network.entity.Postdata;
import com.example.r024network.entity.Userdata;
import com.example.r024network.mapper.PostdataMapper;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostdataMapper postdataMapper;
    private final UserdataMapper userdataMapper;

    @Override
    public void postSingleConfession(Integer account, String title, String content, Integer isAnonymous){
        QueryWrapper<Userdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",account);
        Userdata userdata = userdataMapper.selectOne(queryWrapper);
        Integer userId = userdata.getUserId();
        Postdata postdata = new Postdata();
        postdata.setUserId(userId);
        postdata.setTitle(title);
        postdata.setContent(content);
        postdata.setPublicOrPrivate(isAnonymous);
        postdataMapper.insert(postdata);
    }

    @Override
    public void rewritePost(Integer account, String title, String content){

    }

}
