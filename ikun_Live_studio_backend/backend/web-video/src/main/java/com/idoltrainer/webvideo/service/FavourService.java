package com.idoltrainer.webvideo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idoltrainer.webvideo.domain.Favour;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.domain.Video;

/**
 *
 */
public interface FavourService extends IService<Favour> {

    int doPostFavour(long videoId, User loginUser);

    Page<Video> listFavourVideoByPage(IPage<Video> page, QueryWrapper<Video> queryWrapper, Long id);
}
