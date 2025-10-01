package com.example.r024network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    @JsonProperty("post_id")
    private Integer postId;
    @JsonProperty("user_account")
    private Integer account;
    private String title;
    private String content;
    @JsonProperty("is_public")
    private Integer isAnonymous;
    @JsonProperty("hour")
    private Integer addingHour;
    @JsonProperty("minute")
    private Integer addingMinute;

}
