package com.example.r024network.Service.ServiceImpl;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Service.CommentService;
import com.example.r024network.Service.ImageService;
import com.example.r024network.entity.Comment;
import com.example.r024network.entity.Images;
import com.example.r024network.entity.Userdata;
import com.example.r024network.mapper.CommentMapper;
import com.example.r024network.mapper.ImagesMapper;
import com.example.r024network.mapper.UserdataMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

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

    public void deleteComment(Integer userId, Integer commentId) {
        // 还没有判断是否是自己账号才能删除评论
        Userdata userdata =  userdataMapper.selectOne(wrapperHelper.convert("user_id", userId));
        Comment comment = commentMapper.selectOne(wrapperHelper.convert("comment_id", commentId));
        if (comment == null){
            throw new APIException(410, "评论不存在");
        }
        if (Objects.equals(userId, comment.getUserId())) {
            commentMapper.delete(wrapperHelper.convert("comment_id", commentId));
            //throw new APIException(418, "")
        }
        //commentMapper.delete(wrapperHelper.convert("comment_id", commentId));
    }

    public void postFollowComment(Integer account, String content, Integer postId, Integer followingId) {
        Userdata userdata = userdataMapper.selectOne(wrapperHelper.convert("user_account", account));
        Comment comment = Comment.builder().userId(userdata.getUserId()).parentCommentId(1).followingid(followingId).postId(postId).content(content).build();
        commentMapper.insert(comment);
    }

}
