package com.idoltrainer.webvideo.model.dto.user;

import lombok.Data;

@Data
public class UserAddDTO {
    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 是否被封号
     */
    private String userRole;

}
