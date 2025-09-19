package com.example.r024network.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment {
    private Integer commentId;

    private Integer postId;

    private Integer userId;

    private Integer parentCommentId;

    private String content;

    @JsonProperty("following_id")
    private Integer followingid;
}