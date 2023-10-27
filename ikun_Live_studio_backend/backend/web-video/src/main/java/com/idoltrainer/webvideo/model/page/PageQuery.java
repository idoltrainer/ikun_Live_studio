package com.idoltrainer.webvideo.model.page;

import lombok.Data;
@Data
public class PageQuery {

    /**
     * 当前页号
     */
    private Long current;

    /**
     * 页面大小
     */
    private Long pageSize;

}
