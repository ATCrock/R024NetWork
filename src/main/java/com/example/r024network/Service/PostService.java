package com.example.r024network.Service;
import com.example.r024network.entity.Postdata;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    void postSingleConfession(Integer account, String title, String content, Integer isAnonymous);// 给jwt留位置，后期account统一从jwt获取
    void rewritePost(Integer postId, Integer account, String title, String content, Integer isAnonymous);
    void deletePost(Integer account, Integer postId);
    Postdata[] getAllPost(Integer account);
    void postWithImageParentComment(Integer user_account, String title, String content, Integer isAnonymous, List<MultipartFile> imageFiles);
}
