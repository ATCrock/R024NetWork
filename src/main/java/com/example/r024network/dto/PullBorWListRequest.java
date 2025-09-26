package com.example.r024network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PullBorWListRequest {
    @JsonProperty("user_account")
    private Integer userAccount;
    @JsonProperty("target_account")
    private Integer targetAccount;


}
