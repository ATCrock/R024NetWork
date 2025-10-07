package com.example.r024network.Controller;

import com.example.r024network.Result.AjaxResult;
import com.example.r024network.Service.CommentService;
import com.example.r024network.dto.CommentRequest;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apifox/comment")
@Slf4j
@CrossOrigin(origins = "*")
public class CommentController {
    @Resource
    private CommentService commentService;


    /**回复 帖子
     * @param commentRequest 评论的json流
     * @param request 前端请求，需要包含jwt(从login处获取)
     * @return {@link AjaxResult }
     * 成功返回的data为null，code为200
     */
    @PostMapping("/post_parent_comment")
    public AjaxResult<CommentRequest> parentPost(@Valid @RequestBody CommentRequest commentRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
            commentService.postParentComment(userAccount, commentRequest.getContent(), commentRequest.getPostId());
        return AjaxResult.success();
    }


    /**回复 评论
     * @param commentRequest 评论的json流，比回复帖子需要的
     * @param request 前端请求，需要包含jwt(从login处获取)
     * @return {@link AjaxResult }
     * 成功返回的data为null，code为200
     */
    @PostMapping("/post_follow_comment")
    public AjaxResult<CommentRequest> followPost(@Valid @RequestBody CommentRequest commentRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
            commentService.postFollowComment(userAccount, commentRequest.getContent(), commentRequest.getPostId(), commentRequest.getFollowId());
        return AjaxResult.success();
    }

    /**删除帖子
     * @param commentRequest 评论的json流
     * @param request 前端请求，需要包含jwt(从login处获取)
     * @return {@link AjaxResult }
     * 成功返回的data为null，code为200
     */
    @DeleteMapping("/delete")
    public AjaxResult<CommentRequest> delete(@Valid @RequestBody CommentRequest commentRequest, HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("user_id");
            commentService.deleteComment(userId, commentRequest.getCommentId());
        return AjaxResult.success();
    }

    /**获取帖子
     * @param commentRequest 评论的json流
     * 不需要jwt验证
     * @return {@link AjaxResult }
     * 成功返回的data为null，code为200
     */
    @DeleteMapping("/get")
    public AjaxResult<CommentRequest> getCommend(@Valid @RequestBody CommentRequest commentRequest){
            commentService.listAllComment(commentRequest.getPostId());
        return AjaxResult.success();
    }


}
