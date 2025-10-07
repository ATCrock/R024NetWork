package com.example.r024network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentWithImagesRequest {
    @JsonProperty("user_account")
    private Integer userAccount;
    private String title;
    private String content;
    @JsonProperty("is_public")
    private Integer isAnonymous;
    @JsonProperty("comment_parent_id")
    private Integer commentParentId;
    @JsonProperty("following_id")
    private Integer followId;
    @JsonProperty("comment_id")
    private Integer commentId;
    @JsonProperty("files")
    private MultipartFile[] files;
}
