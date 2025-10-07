package com.example.r024network.Controller;

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

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/apifox/post")
@Slf4j
@CrossOrigin(origins = "*")

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
            postService.postSingleConfession(userAccount,postRequest.getTitle(),postRequest.getContent(),postRequest.getIsAnonymous(),postRequest.getIsPublic());
        return AjaxResult.success();
    }

    /**修改帖子
     * @param postRequest 帖子的json流（用户账号在jwt中存储，需要输入标题，文本，是否匿名，帖子id通过选择帖子来获取）
     * @param request 前后端网络请求，包含jwt
        感觉目前这个东西还是有问题的，需要在前端那边测试一下
     * @return {@link AjaxResult }
     */
    @PutMapping("/rewrite_post")
    public AjaxResult<PostRequest> rewritePost(@Valid @RequestBody PostRequest postRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
            postService.rewritePost(postRequest.getPostId(), userAccount, postRequest.getTitle(),postRequest.getContent(),postRequest.getIsAnonymous(),postRequest.getIsPublic());
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
        postService.deletePost(postRequest.getPostId(), userAccount);
        return  AjaxResult.success();
    }

    /**获取当前账号下能获取的帖子
     * @param request 前后端网络请求，包含jwt
     * @return {@link AjaxResult }
     */
    @GetMapping("/get")
    public AjaxResult<Postdata[]> getPost(@Valid @RequestBody PostRequest postRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
            this.postdata = postService.getAllPost(userAccount);
        return AjaxResult.success(postdata);
    }


    /**没登陆的时候获取所有帖子
     * @return {@link AjaxResult
     */
    @GetMapping("/get2")
    public AjaxResult<Postdata[]> getPost(@Valid @RequestBody PostRequest postRequest){
        this.postdata = postService.getAllPost();
        return AjaxResult.success(postdata);
    }


    /**发带图片的帖子
     * @param title 标题
     * @param content 文本
     * @param isPublic 是否公开
     * @param files 图片文件流
     * @param request 前后端网络请求
     * @return {@link AjaxResult }
     */
    @PostMapping(value = "/post_with_images", consumes ="multipart/form-data")
    public AjaxResult<Object> postWithImages(@Valid @RequestParam String title, String content, Integer is_anonymous, Integer is_public, List<MultipartFile> files, HttpServletRequest request){

        Integer userAccount = (Integer) request.getAttribute("user_account");
        postService.postWithImageParentComment(userAccount, title, content, is_anonymous, is_public, files);
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
        this.postdata = postService.getAllPost(userAccount);
        this.comments = commentService.listAllComment(postRequest.getPostId()).toArray(new Comment[0]);
        return AjaxResult.success(postdata, comments);
    }

    @PostMapping("scheduled_post")
    public AjaxResult<Postdata> postScheduledPost(@Valid @RequestBody PostRequest postRequest, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
        Date date = new Date();
        date = postService.addTime(date, postRequest.getAddingHour(), postRequest.getAddingMinute());
        postService.scheduledPost(userAccount, postRequest.getTitle(),postRequest.getContent(),postRequest.getIsAnonymous(),postRequest.getIsPublic(),date);
        return AjaxResult.success();
    }

    @PostMapping("scheduled_post_with_images")
    public AjaxResult<Postdata> postScheduledPostWithPost(@Valid @RequestParam String title, String content, Integer is_anonymouos, Integer is_public, List<MultipartFile> files, Integer addingHour, Integer addingMinute, HttpServletRequest request){
        Integer userAccount = (Integer) request.getAttribute("user_account");
        Date date = new Date();
        date = postService.addTime(date, addingHour, addingMinute);
        postService.scheduledPostWithImages(userAccount, title,content, is_anonymouos, is_public, date, files);
        return AjaxResult.success();
    }


//    @GetMapping
//    public AjaxResult<Postdata> checkScheduledPost(@Valid @RequestBody PostRequest postRequest, HttpServletRequest request){
//        Integer userAccount = (Integer) request.getAttribute("user_account");
//
//    }
}
