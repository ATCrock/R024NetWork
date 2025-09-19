package com.example.r024network.Controller;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Result.AjaxResult;
import com.example.r024network.Service.CommentService;
import com.example.r024network.dto.CommentRequest;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apifox/comment")
@Slf4j
public class CommentController {
    @Resource
    private CommentService commentService;

    @PostMapping("/post_parent_comment")
    public AjaxResult<CommentRequest> parentPost(@Valid @RequestBody CommentRequest commentRequest){
        try{
            commentService.postParentComment(commentRequest.getUserAccount(), commentRequest.getContent(), commentRequest.getPostId());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }

    @PostMapping("/post_follow_comment")
    public AjaxResult<CommentRequest> followPost(@Valid @RequestBody CommentRequest commentRequest){
        try{
            commentService.postFollowComment(commentRequest.getUserAccount(), commentRequest.getContent(), commentRequest.getPostId(), commentRequest.getFollowId());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }

    @DeleteMapping("/delete")
    public AjaxResult<CommentRequest> delete(@Valid @RequestBody CommentRequest commentRequest){
        try{
            commentService.deleteComment(commentRequest.getPostId());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }

    @DeleteMapping("/get")
    public AjaxResult<CommentRequest> getCommend(@Valid @RequestBody CommentRequest commentRequest){
        try{
            commentService.listAllComment(commentRequest.getPostId());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }


}
