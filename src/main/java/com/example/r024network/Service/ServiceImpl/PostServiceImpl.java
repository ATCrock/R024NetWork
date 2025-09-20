package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.PostService;
import com.example.r024network.Service.UserService;
import com.example.r024network.entity.Postdata;
import com.example.r024network.entity.Userdata;
import com.example.r024network.mapper.PostdataMapper;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostdataMapper postdataMapper;
    private final UserdataMapper userdataMapper;
    private final UserService userService;
    private final WrapperHelper wrapperHelper;

    public int binarySearch(int[] array, int target) {
        int left = 0;
        int right = array.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (array[mid] == target) {
                return mid;
            } else if (array[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1;
    }


    @Override
    public void postSingleConfession(Integer account, String title, String content, Integer isAnonymous){
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", account));
        if (title.isBlank() || content.isBlank()){
            throw new APIException(413, "标题或内容不能为空");
        }
        // 1为公开，0为匿名
        if (! (isAnonymous == 1 || isAnonymous == 0) ){
            throw new APIException(420, "不支持的格式，0为匿名，1为显示名字");
        }
        else{
            postdataMapper.insert(Postdata.builder().userId(userdata.getUserId()).userAccount(String.valueOf(account)).userName(userdata.getUserName()).content(content).publicOrPrivate(isAnonymous).build());
        }
    }

    @Override
    public void rewritePost(Integer postId, Integer account, String title, String content, Integer isAnonymous){
        Postdata post = postdataMapper.selectOne(wrapperHelper.convert("post_id", postId));
        if (post == null){
            throw new APIException(411, "没有对应标题的帖子");
        }else {
            Userdata userdata = userdataMapper.selectById(post.getUserId());
            Userdata userdata2 = userdataMapper.selectOne(wrapperHelper.convert("user_account", account));
            if (Objects.equals(userdata.getUserId(), userdata2.getUserId())) {
                postdataMapper.insert(Postdata.builder().userId(userdata.getUserId()).userName(userdata2.getUserName()).publicOrPrivate(isAnonymous).build());
            }else {
                throw new APIException(412, "你没有发过这个帖子");
            }
        }
    }

    @Override
    public void deletePost(Integer postId, Integer account) {
        // 用户自己的账号
        QueryWrapper<Postdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account",account);
        queryWrapper.eq("post_id",postId);
        Postdata post = postdataMapper.selectOne(queryWrapper);

        if (post == null){
            throw new APIException(411, "没有对应的帖子");
        }else {
            Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", account));
            if (Objects.equals(post.getUserId(), userdata.getUserId())) {
                postdataMapper.delete(queryWrapper);
            }else {
                throw new APIException(412, "你没有发过这个帖子");
            }
        }
    }

    @Override
    public Postdata[] getAllPost(Integer account) {
        // 用户自己的账号
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", account));
        if (userdata == null){
            throw new APIException(410, "未找到该账号");
        }else {
            List<Postdata> postList = postdataMapper.selectList(null);
            Postdata[] retPostdata = new Postdata[postList.size()];
            int count = 0;
            int[] blackList = userService.getBlack(String.valueOf(account));
            blackList = Arrays.copyOf(userService.getBlack(String.valueOf(account)), blackList.length);
            for (Postdata postdata : postList) {
                if (binarySearch(blackList, postdata.getUserId()) == -1) {         // -1相当于没找到对应用户id
                    if (postdata.getPublicOrPrivate() == 0) {         // 如果发帖子的人不在用户黑名单里面
                        if (!Objects.equals(postdata.getUserId(), userdata.getUserId())) {
                                postdata.setUserName("anonymity");
                            }
                            retPostdata[count] = postdata;
                            count++;

                    }else{
                        retPostdata[count] = postdata;
                        count++;
                    }
                }
            }
            return retPostdata;
        }
    }
}
