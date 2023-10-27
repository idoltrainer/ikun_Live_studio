package com.idoltrainer.webvideo.model.dto.video;

import lombok.Data;

import java.util.List;

@Data
public class VideoAddDTO {
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
    private List<String> tags;
}
