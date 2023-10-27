package com.idoltrainer.webvideo.service;

import com.idoltrainer.webvideo.domain.Relations;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idoltrainer.webvideo.domain.User;

/**
 *
 */
public interface RelationsService extends IService<Relations> {

    int focus(Long userId, User loginUser);
}
