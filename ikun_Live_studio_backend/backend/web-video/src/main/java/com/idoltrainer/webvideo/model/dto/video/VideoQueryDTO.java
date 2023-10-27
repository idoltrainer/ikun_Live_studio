package com.idoltrainer.webvideo.model.dto.video;

import com.idoltrainer.webvideo.model.page.PageQuery;
import lombok.Data;

@Data
public class VideoQueryDTO extends PageQuery {

    /**
     * 搜索内容
     */
    private String searchText;

    /**
     * 用户id
     */
    private Long userId;
}
