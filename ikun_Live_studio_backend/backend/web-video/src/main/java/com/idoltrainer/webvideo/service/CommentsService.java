package com.idoltrainer.webvideo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.idoltrainer.webvideo.domain.Comments;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface CommentsService extends IService<Comments> {

    List<Comments> selectCommentedVideoId(QueryWrapper queryWrapper);
}
