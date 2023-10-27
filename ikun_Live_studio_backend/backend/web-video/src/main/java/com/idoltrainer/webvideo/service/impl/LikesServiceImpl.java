package com.idoltrainer.webvideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.domain.Likes;

import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.domain.Video;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.mapper.LikesMapper;
import com.idoltrainer.webvideo.service.LikesService;
import com.idoltrainer.webvideo.service.UserService;
import com.idoltrainer.webvideo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class LikesServiceImpl extends ServiceImpl<LikesMapper, Likes>
    implements LikesService {

    @Autowired
    private VideoService videoService;

    @Resource
    private UserService userService;

    @Override
    public int doVideoLike(long videoId, User loginUser) {
        //创建新的点赞
        Likes likes = new Likes();
        likes.setUserId(loginUser.getId());
        likes.setVideoId(videoId);
        QueryWrapper<Likes> likesQueryWrapper = new QueryWrapper<>(likes);
        Likes oldLikes = this.getOne(likesQueryWrapper);
        //获取视频的用户
        Video video = videoService.getById(videoId);
        boolean result;
        // 已点赞
        if (oldLikes != null) {
            result = this.remove(likesQueryWrapper);
            if (result) {
                // 点赞数 - 1
                videoService.update()
                        .eq("id", videoId)
                        .gt("likesNum", 0)
                        .setSql("likesNum = likesNum - 1")
                        .update();
                result = userService.update()
                        .eq("id", video.getUserId())
                        .gt("likesNum", 0)
                        .setSql("likesNum = likesNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(likes);
            if (result) {
                // 点赞数 + 1
                videoService.update()
                        .eq("id", videoId)
                        .setSql("likesNum = likesNum + 1")
                        .update();
                result = userService.update()
                        .eq("id", video.getUserId())
                        .setSql("likesNum = likesNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}




