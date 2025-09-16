package com.example.r024network.entity;

import lombok.Data;

/**
 * @TableName comment
 */
@Data
public class Comment {
    private Integer commentId;

    private Integer postId;

    private Integer userId;

    private Integer parentCommentId;

    private String content;
}