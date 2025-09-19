package com.example.r024network.Controller;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Result.AjaxResult;
import com.example.r024network.Service.PostService;
import com.example.r024network.Service.UserService;
import com.example.r024network.dto.*;
import com.example.r024network.entity.Postdata;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apifox/post")
@Slf4j
public class PostController {
    @Resource
    private PostService postService;
    Postdata[] postdata;
    @PostMapping("/post")
    public AjaxResult<PostRequest> register(@Valid @RequestBody PostRequest postRequest){
        try{
            postService.postSingleConfession(postRequest.getAccount(),postRequest.getTitle(),postRequest.getContent(),postRequest.getIsAnonymous());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }

    @PutMapping("/rewrite_post")
    public AjaxResult<PostRequest> rewritePost(@Valid @RequestBody PostRequest postRequest){
        try{
            postService.rewritePost(postRequest.getPostId(), postRequest.getAccount(),postRequest.getTitle(),postRequest.getContent(),postRequest.getIsAnonymous());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return  AjaxResult.success();
    }

    @DeleteMapping("/delete")
    public AjaxResult<PostRequest> deletePost(@Valid @RequestBody PostRequest postRequest){
        try{
            postService.deletePost(postRequest.getPostId(), postRequest.getAccount());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return  AjaxResult.success();
    }

    @GetMapping("/get")
    public AjaxResult<Postdata[]> getPost(@Valid @RequestBody PostRequest postRequest){
        try{
            this.postdata = postService.getAllPost(postRequest.getAccount());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success(postdata);
    }
}
