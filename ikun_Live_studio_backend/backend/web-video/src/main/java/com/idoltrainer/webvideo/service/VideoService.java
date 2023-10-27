package com.idoltrainer.webvideo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idoltrainer.webvideo.domain.Video;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idoltrainer.webvideo.model.dto.video.VideoQueryDTO;
import com.idoltrainer.webvideo.model.vo.video.VideoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface VideoService extends IService<Video> {

    void validVideo(Video video, boolean b);

    QueryWrapper<Video> getQueryWrapper(VideoQueryDTO videoQueryDTO);

    Page<VideoVO> getVideoVOPage(Page<Video> postPage, HttpServletRequest request);

    Page<Video> searchFromEs(VideoQueryDTO videoQueryDTO);

    boolean sendFans(Long userId,Long videoId);

    QueryWrapper<Video> getUserQueryWrapper(VideoQueryDTO videoQueryDTO);

    QueryWrapper<Video> getFavourQueryWrapper(VideoQueryDTO videoQueryDTO);

    List<Video> getPageAsList(Integer current, int size);
}
