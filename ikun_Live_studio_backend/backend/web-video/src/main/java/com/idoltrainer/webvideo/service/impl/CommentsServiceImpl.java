package com.idoltrainer.webvideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idoltrainer.webvideo.domain.Comments;
import com.idoltrainer.webvideo.service.CommentsService;
import com.idoltrainer.webvideo.mapper.CommentsMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 */
@Service
public class CommentsServiceImpl extends ServiceImpl<CommentsMapper, Comments>
    implements CommentsService{

    @Resource
    private CommentsMapper commentsMapper;

    @Override
    public List<Comments> selectCommentedVideoId(QueryWrapper queryWrapper) {
        return commentsMapper.selectList(queryWrapper);
    }
}




