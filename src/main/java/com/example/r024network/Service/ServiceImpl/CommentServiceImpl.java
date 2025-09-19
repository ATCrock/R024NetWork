package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.Service.CommentService;
import com.example.r024network.entity.Comment;
import com.example.r024network.entity.Postdata;
import com.example.r024network.entity.Userdata;
import com.example.r024network.mapper.CommentMapper;
import com.example.r024network.mapper.ImagesMapper;
import com.example.r024network.mapper.PostdataMapper;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    // parent_id为0则代表为回复评论
    private final PostdataMapper postdataMapper;
    private final UserdataMapper userdataMapper;
    private final ImagesMapper imagesMapper;
    private final CommentMapper commentMapper;
    public void postParentComment(Integer account, String content, Integer postId) {
        QueryWrapper<Userdata> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", account);
        Userdata userdata =  userdataMapper.selectOne(userQueryWrapper);
        Comment comment = new Comment();
        comment.setParentCommentId(0);
        comment.setUserId(userdata.getUserId());
        comment.setPostId(postId);
        comment.setContent(content);
        commentMapper.insert(comment);
    }

    public List<Comment> listAllComment(Integer postId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId);
        return commentMapper.selectList(queryWrapper);
    }

    public void deleteComment(Integer commentId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", commentId);
        commentMapper.delete(queryWrapper);
    }

    public void postFollowComment(Integer account, String content, Integer postId, Integer followingId) {
        QueryWrapper<Userdata> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("user_account", account);
        Userdata userdata =  userdataMapper.selectOne(userQueryWrapper);
        Comment comment = new Comment();
        comment.setUserId(userdata.getUserId());
        comment.setParentCommentId(1);// 0主评论，非0都是副评论
        comment.setFollowingid(followingId);
        comment.setPostId(postId);
        comment.setContent(content);
        commentMapper.insert(comment);
    }
}
