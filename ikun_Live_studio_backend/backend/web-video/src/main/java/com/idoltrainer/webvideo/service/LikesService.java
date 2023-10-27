package com.idoltrainer.webvideo.service;

import com.idoltrainer.webvideo.domain.Likes;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idoltrainer.webvideo.domain.User;

/**
 *
 */
public interface LikesService extends IService<Likes> {

    int doVideoLike(long postId, User loginUser);

}
