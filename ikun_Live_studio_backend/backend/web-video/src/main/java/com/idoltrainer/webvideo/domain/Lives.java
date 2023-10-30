package com.idoltrainer.webvideo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.lettuce.core.StrAlgoArgs;
import lombok.Data;

/**
 * 直播间
 * @TableName lives
 */
@TableName(value ="lives")
@Data
public class Lives implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 直播间详情
     */
    private String content;

    /**
     * 主播 id
     */
    private Long anchorId;

    /**
     * 访客 id
     */
    private String visitId;

    /**
     * 直播间人数
     */
    private Long totalNum;

    /**
     * 是否开启直播
     */
    private Byte isLive;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}