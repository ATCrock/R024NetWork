package com.example.r024network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("user_account")
    private Integer userAccount;
    @JsonProperty("user_name")
    private String userName;
    private String password;
    @JsonProperty("user_type")
    private Integer userType;

}
