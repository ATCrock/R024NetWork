package com.example.r024network.Service;
import com.example.r024network.entity.Postdata;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public interface PostService {
    void postSingleConfession(Integer account, String title, String content, Integer isAnonymous, Integer isPublic);// 给jwt留位置，后期account统一从jwt获取
    void rewritePost(Integer postId, Integer account, String title, String content, Integer isAnonymous, Integer isPublic);
    void deletePost(Integer account, Integer postId);
    Postdata[] getAllPost();
    Postdata[] getAllPost(Integer account);
    void postWithImageParentComment(Integer user_account, String title, String content, Integer isAnonymous, Integer isPublic, List<MultipartFile> imageFiles);
    void scheduledPost(Integer user_account, String title, String content, Integer isAnonymous, Integer isPublic, Date publishTime);
    void checkScheduledPost();
    Date addTime(Date date, Integer addingHour, Integer addingMinute);
    void scheduledPostWithImages(Integer user_account, String title, String content, Integer isAnonymous, Integer isPublic, Date publishTime, List<MultipartFile> imageFiles);
}
