package com.idoltrainer.webvideo.model.dto.user;

import lombok.Data;

/**
 * 用户登录封装类
 */
@Data
public class UserLoginDTO {

    /**
     * 用户名
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
