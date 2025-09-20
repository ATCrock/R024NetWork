package com.example.r024network.Service.ServiceImpl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.CommentService;
import com.example.r024network.dto.CommentRequest;
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
    private final UserdataMapper userdataMapper;
    private final CommentMapper commentMapper;
    private final WrapperHelper wrapperHelper;
    public void postParentComment(Integer account, String content, Integer postId) {
        Userdata userdata =  userdataMapper.selectOne(wrapperHelper.convert("user_account", account));
        Comment comment = new Comment();
        comment.setParentCommentId(0);
        comment.setUserId(userdata.getUserId());
        comment.setPostId(postId);
        comment.setContent(content);
        commentMapper.insert(comment);
    }

    public List<Comment> listAllComment(Integer postId) {
        return commentMapper.selectList(wrapperHelper.convert("post_id", postId));
    }

    public void deleteComment(Integer commentId) {
        Comment comment = commentMapper.selectOne(wrapperHelper.convert("comment_id", commentId));
        if (comment == null){
            throw new APIException(410, "评论不存在");
        }
        commentMapper.delete(wrapperHelper.convert("comment_id", commentId));
    }

    public void postFollowComment(Integer account, String content, Integer postId, Integer followingId) {
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", account));
        Comment comment = new Comment();
        comment.setUserId(userdata.getUserId());
        comment.setParentCommentId(1);// 0主评论，非0都是副评论
        comment.setFollowingid(followingId);
        comment.setPostId(postId);
        comment.setContent(content);
        commentMapper.insert(comment);
    }
}
