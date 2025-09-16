package com.example.r024network.entity;

import lombok.Data;

/**
 * @TableName images
 */
@Data
public class Images {
    private String picId;

    private Integer postId;

    private Integer userId;

    private String filePath;

    private String fileName;

    private String fileSize;

    private String picType;

    private Integer isAvator;
}