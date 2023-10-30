package com.idoltrainer.webvideo.model.recommend;


import lombok.Data;


/**
 * 用户推荐算法封装类
 */
@Data
public class UserRecommend {


    /**
     * user的id
     */
    private Long userId;


    /**
     * 视频的id
     */
    private Long[] videoId;


    /**
     * 比较值
     */
    private Double cos_th;

}
