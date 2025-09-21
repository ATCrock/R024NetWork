package com.example.r024network.Service;

import com.example.r024network.entity.Comment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CommentService {
    void postParentComment(Integer account, String content, Integer postId);
    void postFollowComment(Integer account, String content, Integer postId, Integer followingId);
    List<Comment> listAllComment(Integer postId);
    void deleteComment(Integer commentId);
    //void postWithImageParentComment(Integer account, String content, Integer postId, MultipartFile[] files);
}
