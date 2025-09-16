package com.example.r024network.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@TableName(value = "postdata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Postdata {
    @JsonProperty("post_id")
    private Integer postId;
    @JsonProperty("user_id")
    private Integer userId;

    private String title;

    private String content;
    @JsonProperty("is_public")
    private Integer publicOrPrivate;
}