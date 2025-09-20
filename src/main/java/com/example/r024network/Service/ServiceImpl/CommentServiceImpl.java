package com.example.r024network.Service.ServiceImpl;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.CommentService;
import com.example.r024network.entity.Comment;
import com.example.r024network.entity.Userdata;
import com.example.r024network.mapper.CommentMapper;
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
        Comment comment = Comment.builder().userId(userdata.getUserId()).parentCommentId(0).postId(postId).content(content).build();
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
        Comment comment = Comment.builder().userId(userdata.getUserId()).parentCommentId(1).followingid(followingId).postId(postId).content(content).build();
        commentMapper.insert(comment);
    }
}
