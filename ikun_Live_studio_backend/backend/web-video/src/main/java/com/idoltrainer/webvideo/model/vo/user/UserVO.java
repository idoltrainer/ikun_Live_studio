package com.idoltrainer.webvideo.model.vo.user;

import lombok.Data;

/**
 *
 * 返回脱敏的用户：因为用户有一些敏感字段是不能返回给前端的，我们需要给他隐藏
 */
@Data
public class UserVO {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userContent;

    /**
     * 是否被封号
     */
    private String userRole;
}
