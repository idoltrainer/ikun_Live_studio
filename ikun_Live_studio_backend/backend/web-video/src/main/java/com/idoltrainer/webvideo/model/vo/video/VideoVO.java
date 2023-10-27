package com.idoltrainer.webvideo.model.vo.video;

import cn.hutool.json.JSONUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.idoltrainer.webvideo.domain.Video;
import com.idoltrainer.webvideo.model.vo.user.UserVO;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import java.util.Date;
import java.util.List;

@Data
public class VideoVO {

    private final static Gson GSON = new Gson();
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

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

    /**
     * 点赞数
     */
    private Integer likesNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 用户封装对象
     */
    private UserVO userVO;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否点赞
     */
    private Boolean isLike;

    /**
     * 是否收藏
     */
    private Boolean isFavour;




    public static VideoVO objToVo(Video video) {
        if (video == null) {
            return null;
        }
        VideoVO videoVO = new VideoVO();
        BeanUtils.copyProperties(video, videoVO);
        videoVO.setTags(GSON.fromJson(video.getTags(), new TypeToken<List<String>>() {
        }.getType()));
        return videoVO;

    }
}
