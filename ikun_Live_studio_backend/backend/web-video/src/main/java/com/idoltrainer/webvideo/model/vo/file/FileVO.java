package com.idoltrainer.webvideo.model.vo.file;

import lombok.Data;

@Data
public class FileVO {


    /**
     * 视频地址，存储到oss
     */
    private String playUrl;

    /**
     * 封面地址，存储到oss
     */
    private String coverUrl;

}
