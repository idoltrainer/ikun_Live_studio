package com.idoltrainer.webvideo.model.dto.user;

import com.idoltrainer.webvideo.model.page.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
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
