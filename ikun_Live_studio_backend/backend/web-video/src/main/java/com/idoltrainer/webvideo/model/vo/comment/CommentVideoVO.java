package com.idoltrainer.webvideo.model.vo.comment;


import lombok.Data;

import java.util.Date;

@Data
public class CommentVideoVO {

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     *  视频id
     */
    private Long videoId;
}
