package com.idoltrainer.webvideo.model.dto.user;

import lombok.Data;

/**
 * 用户注册封装类
 */
@Data
public class UserRegisterDTO {

    /**
     * 用户名
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 再次输入密码，和密码进行比对
     */
    private String checkPassword;


}
