package com.example.r024network.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value ="images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Images {
    @JsonProperty("image_id")
    @TableId(type = IdType.AUTO)
    private String picId;
    @JsonProperty("post_id")
    private String postId;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("file_path")
    private String filePath;
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("file_size")
    private String fileSize;
    @JsonProperty("file_SHA256")
    private String fileType;
    @JsonProperty("is_protract")
    private Integer isAvator;
}