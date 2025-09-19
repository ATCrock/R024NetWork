package com.example.r024network.entity;

import lombok.Data;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@TableName(value = "postdata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postdata {
    @TableId(type = IdType.AUTO)
    @JsonProperty("post_id")
    private Integer postId;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("account")
    private String userAccount;

    @JsonProperty("user_name")
    private String userName;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;
    @JsonProperty("is_public")
    private Integer publicOrPrivate;
    private Integer comment_id;
}