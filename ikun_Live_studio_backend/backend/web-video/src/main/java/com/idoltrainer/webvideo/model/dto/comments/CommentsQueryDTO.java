package com.idoltrainer.webvideo.model.dto.comments;


import com.idoltrainer.webvideo.model.page.PageQuery;
import lombok.Data;

@Data
public class CommentsQueryDTO extends PageQuery {

    /**
     * 视频id
     */
    private Long videoId;
}
