package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.ImageService;
import com.example.r024network.Service.PostService;
import com.example.r024network.Service.UserService;
import com.example.r024network.entity.Images;
import com.example.r024network.entity.Postdata;
import com.example.r024network.entity.Userdata;
import com.example.r024network.mapper.ImagesMapper;
import com.example.r024network.mapper.PostdataMapper;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostdataMapper postdataMapper;
    private final UserdataMapper userdataMapper;
    private final UserService userService;
    private final WrapperHelper wrapperHelper;
    private final ImageService imageService;
    private final ImagesMapper imagesMapper;

    public int binarySearch(int[] array, int target) {
        // 二分法查找
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
            throw new APIException(411, "标题或内容不能为空");
        }
        // 1为公开，0为匿名
        if (! (isAnonymous == 1 || isAnonymous == 0) ){
            throw new APIException(420, "不支持的格式，0为匿名，1为显示名字");
        }
        else{
            postdataMapper.insert(Postdata.builder().userId(userdata.getUserId()).userAccount(String.valueOf(account)).userName(userdata.getUserName()).title(title).content(content).publicOrPrivate(isAnonymous).status(2).build());
        }
    }

    @Override
    public void rewritePost(Integer postId, Integer account, String title, String content, Integer isAnonymous){
        Postdata post = postdataMapper.selectOne(wrapperHelper.convert("post_id", postId));
        if (post == null){
            throw new APIException(410, "没有对应的帖子");
        }else {
            Userdata userdata = userdataMapper.selectById(post.getUserId());
            Userdata userdata2 = userdataMapper.selectOne(wrapperHelper.convert("user_account", account));
            if (Objects.equals(userdata.getUserId(), userdata2.getUserId())) {
                postdataMapper.insert(Postdata.builder().userId(userdata.getUserId()).userName(userdata2.getUserName()).publicOrPrivate(isAnonymous).build());
            }else {
                throw new APIException(410, "没有对应的帖子");
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
            throw new APIException(410, "没有对应的帖子");
        }else {
            Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", account));
            if (Objects.equals(post.getUserId(), userdata.getUserId())) {
                postdataMapper.delete(queryWrapper);
            }else {
                throw new APIException(410, "你没有发过这个帖子");
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
            QueryWrapper<Postdata>  queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status",2); // 只查询已经发布的帖子
            List<Postdata> postList = postdataMapper.selectList(queryWrapper);
            Postdata[] retPostdata = new Postdata[postList.size()];// 扫到几个就给几个位置
            int count = 0;// 循环变量
            int[] blackList = userService.getBlack(account);
            blackList = Arrays.copyOf(userService.getBlack(account), blackList.length);
            for (Postdata postdata : postList) {
                if (binarySearch(blackList, postdata.getUserId()) == -1) {         // -1相当于没找到对应用户id
                    if (postdata.getPublicOrPrivate() == 0) {         // 如果发帖子的人匿名
                        if (!Objects.equals(postdata.getUserId(), userdata.getUserId())) { // 非用户自己看到帖子
                                postdata.setUserName("user");// 不改变数据库内数据，只改变了传输数据
                            }
                    }
                    retPostdata[count] = postdata; // (如果是用户自己看到自己发的帖子，不改变内容)塞进retpostdata
                    count++;
                }
            }
            return retPostdata;
        }
    }

    public String[] storeListFiles(List<MultipartFile> imageFiles){
        if(imageFiles.size() > 9){
            throw new APIException(412, "不能上传大于9张图");
        }else {
            String[] fileNameArrays = new String[imageFiles.size()];
            int count = 0;
            for(MultipartFile file : imageFiles){
                // 批量存储文件
                String tempFileC = imageService.storeFile(file);
                // 如果存储成功，会返回文件名
                if (tempFileC != null){
                    fileNameArrays[count] = tempFileC;
                    count++;
                }
            }
            return fileNameArrays;
        }
    }


    public void postWithImageParentComment(Integer user_account, String title, String content, Integer isAnonymous, List<MultipartFile> imageFiles){
        String[] fileNameArrays = storeListFiles(imageFiles);
        if (fileNameArrays[0] != null) {
            Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", user_account));
            Postdata postdata = Postdata.builder().userId(userdata.getUserId()).userAccount(String.valueOf(user_account)).userName(userdata.getUserName()).title(title).content(content).publicOrPrivate(isAnonymous).status(2).build();
            postdataMapper.insert(postdata);
            for (String fileNameArray : fileNameArrays) {
                // 再把这个帖子的id赋给每个图片
                Images images = imagesMapper.selectOne(wrapperHelper.convert("file_name", fileNameArray));
                images.setUserId(userdata.getUserId());
                String tempId = images.getPostId();
                String blendingId;
                if (Objects.equals(tempId, "null")) {
                    blendingId = images.getPostId() + ";" + postdata.getPostId();
                }else {
                    blendingId = String.valueOf(postdata.getPostId());
                }
                images.setPostId(blendingId);
                imagesMapper.insertOrUpdate(images);
                }
            }
        }


    public void scheduledPost(Integer user_account, String title, String content, Integer isAnonymous, Date publishTime){
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", user_account));
        Postdata post = Postdata.builder().userId(userdata.getUserId()).userAccount(String.valueOf(user_account)).userName(userdata.getUserName()).title(title).content(content).publicOrPrivate(isAnonymous).status(1).scheduleTick(publishTime).build();
        // 标记为定时发布, schedule_tick为秒数（date是毫秒数）
        postdataMapper.insert(post);
    }

    public void scheduledPostWithImages(Integer user_account, String title, String content, Integer isAnonymous, Date publishTime, List<MultipartFile> imageFiles){
        String[] fileNameArrays = storeListFiles(imageFiles);
        if (fileNameArrays[0] != null) {
            Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", user_account));
            Postdata postdata = Postdata.builder().userId(userdata.getUserId()).userAccount(String.valueOf(user_account)).userName(userdata.getUserName()).title(title).content(content).publicOrPrivate(isAnonymous).status(1).scheduleTick(publishTime).build();
            postdataMapper.insert(postdata);
            for (String fileNameArray : fileNameArrays) {
                // 再把这个帖子的id赋给每个图片
                Images images = imagesMapper.selectOne(wrapperHelper.convert("file_name", fileNameArray));
                images.setUserId(userdata.getUserId());
                String tempId = images.getPostId();
                String blendingId;
                if (Objects.equals(tempId, "null")) {
                    blendingId = images.getPostId() + ";" + postdata.getPostId();
                }else {
                    blendingId = String.valueOf(postdata.getPostId());
                }
                images.setPostId(blendingId);
                imagesMapper.insertOrUpdate(images);
            }
        }
    }

    public void checkScheduledPost(){
        QueryWrapper<Postdata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1).le("schedule_tick", new Date());// 获取未发布帖子，对比时间
        List<Postdata> scheduledPosts = postdataMapper.selectList(queryWrapper);
        if(scheduledPosts.isEmpty()){
            System.out.println("没有等待执行的任务");
        }
        for(Postdata postdata : scheduledPosts){
            postdata.setStatus(2);
            postdataMapper.updateById(postdata);
        }

        // le:less than or equal;
        // ge:greater than or equal;
    }

    public Date addTime(Date date, Integer addingHour, Integer addingMinute){
        // 将date的值添加指定小时与分钟
        LocalDateTime localDateTime = date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        localDateTime = localDateTime.plusHours(addingHour).plusMinutes(addingMinute);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
