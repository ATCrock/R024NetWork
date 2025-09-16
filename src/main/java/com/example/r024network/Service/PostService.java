package com.example.r024network.Service;


public interface PostService {
    void postSingleConfession(Integer account, String title, String content, Integer isAnonymous);// 给jwt留位置，后期account统一从jwt获取
    void rewritePost(Integer postId, Integer account, String title, String content);
    void deletePost(Integer postId);
    void getAllPost();

}
