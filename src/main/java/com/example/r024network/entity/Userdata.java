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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Userdata {
    @TableId(type = IdType.AUTO)
    @JsonProperty("user_id")
    private Integer userId;

    private Integer userAccount;

    private String userName;

    private String userPassword;

    private Integer userType;

    private String userHeadPortraitAddress;
}