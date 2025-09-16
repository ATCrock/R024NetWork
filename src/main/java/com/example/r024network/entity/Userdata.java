package com.example.r024network.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName(value = "userdata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Userdata {
    @JsonProperty("user_id")
    @TableId(type = IdType.AUTO)
    private Integer userId;

    @JsonProperty("user_account")
    private String userAccount;
    @JsonProperty("name")
    private String userName;
    @JsonProperty("password")
    private String userPassword;
    @JsonProperty("user_type")
    private Integer userType;
    @JsonProperty("head_portrait")
    private String userHeadPortraitAddress;
    @JsonProperty("black_list")
    private String blackList = "0";
}