package com.example.r024network.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpadteInformationRequest {
    @JsonProperty("prev_account")
    private Integer prevAccount;
    @JsonProperty("new_account")
    private Integer latestUserAccount;
    @JsonProperty("new_name")
    private String userName;
    @JsonProperty("new_password")
    private String password;
    private String userHeadPortraitAddress;
}
