package com.idoltrainer.webvideo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.idoltrainer.webvideo.model.vo.user.UserVO;
import lombok.Data;

/**
 * 视频
 * @TableName video
 */
@TableName(value ="video")
@Data
public class Video implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 详情
     */
    private String content;

    /**
     * 视频地址，存储到oss
     */
    private String playUrl;

    /**
     * 封面地址，存储到oss
     */
    private String coverUrl;

    /**
     * 标签列表（json 数组）
     */
    private String tags;

    /**
     * 点赞数
     */
    private Long likesNum;

    /**
     * 收藏数
     */
    private Long favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除,视频也使用逻辑删除
     */
    private Byte isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}