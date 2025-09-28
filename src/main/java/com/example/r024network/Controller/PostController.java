package com.example.r024network.Controller;

import com.example.r024network.Exception.APIException;
import com.example.r024network.Result.AjaxResult;
import com.example.r024network.Service.CommentService;
import com.example.r024network.Service.PostService;
import com.example.r024network.dto.*;
import com.example.r024network.entity.Comment;
import com.example.r024network.entity.Postdata;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/apifox/post")
@Slf4j
public class PostController {
    @Resource
    private PostService postService;
    @Resource
    private CommentService commentService;
    Postdata[] postdata;
    Comment[] comments;

    /**只发帖子（后续考虑与带图片发帖子合并）
     * @param postRequest 帖子的json流（用户账号在jwt中存储，需要输入标题，文本，是否匿名）
     * @param request 前后端网络请求，包含jwt
     * @return {@link AjaxResult }
     */
    @PostMapping("/post")
    public AjaxResult<PostRequest> post(@Valid @RequestBody PostRequest postRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
        try{
            postService.postSingleConfession(userAccount,postRequest.getTitle(),postRequest.getContent(),postRequest.getIsAnonymous());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }

    /**修改帖子
     * @param postRequest 帖子的json流（用户账号在jwt中存储，需要输入标题，文本，是否匿名，帖子id通过选择帖子来获取）
     * @param request 前后端网络请求，包含jwt
     * @return {@link AjaxResult }
     */
    @PutMapping("/rewrite_post")
    public AjaxResult<PostRequest> rewritePost(@Valid @RequestBody PostRequest postRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
        try{
            postService.rewritePost(postRequest.getPostId(), userAccount, postRequest.getTitle(),postRequest.getContent(),postRequest.getIsAnonymous());
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return  AjaxResult.success();
    }

    /**删除帖子
     * @param postRequest 帖子的json流（用户账号在jwt中存储，帖子id通过选择帖子来获取）
     * @param request 前后端网络请求，包含jwt
     * @return {@link AjaxResult }
     */
    @DeleteMapping("/delete")
    public AjaxResult<PostRequest> deletePost(@Valid @RequestBody PostRequest postRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
        try{
            postService.deletePost(postRequest.getPostId(), userAccount);
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return  AjaxResult.success();
    }

    /**获取当前账号帖子
     * @param request 前后端网络请求，包含jwt
     * @return {@link AjaxResult }
     */
    @GetMapping("/get")
    public AjaxResult<Postdata[]> getPost(@Valid @RequestBody HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
        try{
            this.postdata = postService.getAllPost(userAccount);
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success(postdata);
    }


    /**发带图片的帖子
     * @param title 标题
     * @param content 文本
     * @param is_public 是否匿名
     * @param files 图片文件流
     * @param request 前后端网络请求
     * @return {@link AjaxResult }
     */
    @PostMapping(value = "/post_with_images", consumes ="multipart/form-data")
    public AjaxResult<Object> postWithImages(@Valid @RequestParam String title, String content, Integer is_public, List<MultipartFile> files, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
        try {
            postService.postWithImageParentComment(userAccount, title, content, is_public, files);
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success();
    }


    /**
     *
     * @param postRequest 理论上只需要postId，通过前端获取
     * @param request 前后端网络请求
     * @return {@link AjaxResult }
     */
    @GetMapping("/get_post_and_comments")
    public AjaxResult<Object[]> getPostAndComments(@Valid @RequestBody PostRequest postRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
        try{
            this.postdata = postService.getAllPost(userAccount);
            this.comments = commentService.listAllComment(postRequest.getPostId()).toArray(new Comment[0]);
        }catch (APIException e){
            return AjaxResult.fail(e.getStatusCode(), e.getErrorMessage());
        }
        return AjaxResult.success(postdata, comments);
    }

}
