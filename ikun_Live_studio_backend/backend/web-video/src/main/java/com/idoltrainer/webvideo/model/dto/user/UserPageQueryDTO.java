package com.idoltrainer.webvideo.model.dto.user;

import com.idoltrainer.webvideo.model.page.PageQuery;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserPageQueryDTO extends PageQuery implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 简介
     */
    private String userContent;

    /**
     * 是否封号
     */
    private String userRole;


}
