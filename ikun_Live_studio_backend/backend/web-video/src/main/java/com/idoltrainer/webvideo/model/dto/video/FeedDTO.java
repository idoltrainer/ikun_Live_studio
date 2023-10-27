package com.idoltrainer.webvideo.model.dto.video;

import lombok.Data;

/**
 * 用于feed流查询
 */
@Data
public class FeedDTO {

    /**
     * 时间戳
     */
    private Long lastId;

    /**
     * 一次查询的视频数
     */
    private Long size;
}
