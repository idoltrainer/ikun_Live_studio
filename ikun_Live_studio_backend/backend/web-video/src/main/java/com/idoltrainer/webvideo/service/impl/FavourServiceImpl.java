package com.idoltrainer.webvideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.domain.Favour;

import com.idoltrainer.webvideo.domain.Likes;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.domain.Video;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.mapper.FavourMapper;
import com.idoltrainer.webvideo.service.FavourService;
import com.idoltrainer.webvideo.service.VideoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class FavourServiceImpl extends ServiceImpl<FavourMapper, Favour>
    implements FavourService {

    @Resource
    private VideoService videoService;

    /**
     * 收藏、不收藏
     * @param videoId
     * @param loginUser
     * @return
     */
    @Override
    public int doPostFavour(long videoId, User loginUser) {
        //创建新的点赞
        Favour favour = new Favour();
        favour.setUserId(loginUser.getId());
        favour.setVideoId(videoId);
        QueryWrapper<Favour> favourQueryWrapper = new QueryWrapper<>(favour);
        Favour oldFavour = this.getOne(favourQueryWrapper);
        //获取视频的用户
        Video video = videoService.getById(videoId);
        boolean result;
        // 已点赞
        if (oldFavour != null) {
            result = this.remove(favourQueryWrapper);
            if (result) {
                // 点赞数 - 1
                videoService.update()
                        .eq("id", videoId)
                        .gt("favourNum", 0)
                        .setSql("favourNum = favourNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            result = this.save(favour);
            if (result) {
                // 点赞数 + 1
                videoService.update()
                        .eq("id", videoId)
                        .setSql("favourNum = favourNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }

    @Override
    public Page<Video> listFavourVideoByPage(IPage<Video> page, QueryWrapper<Video> queryWrapper, Long id) {
        if (id <= 0) {
            return new Page<>();
        }
        return baseMapper.listFavourPostByPage(page, queryWrapper, id);
    }
}




