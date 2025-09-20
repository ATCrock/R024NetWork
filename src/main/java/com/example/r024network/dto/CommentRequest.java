package com.example.r024network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequest {
    @JsonProperty("post_id")
    private int postId;
    @JsonProperty("user_account")
    private Integer userAccount;
    private String content;
    @JsonProperty("comment_parent_id")
    private Integer commentParentId;
    @JsonProperty("following_id")
    private Integer followId;
    @JsonProperty("comment_id")
    private Integer commentId;

}
