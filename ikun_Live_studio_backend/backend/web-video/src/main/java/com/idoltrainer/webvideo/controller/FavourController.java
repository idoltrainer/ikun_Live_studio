package com.idoltrainer.webvideo.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idoltrainer.webvideo.common.BaseResponse;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.common.ResultUtils;
import com.idoltrainer.webvideo.domain.Favour;
import com.idoltrainer.webvideo.domain.Likes;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.domain.Video;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.model.dto.video.VideoQueryDTO;
import com.idoltrainer.webvideo.model.vo.video.VideoVO;
import com.idoltrainer.webvideo.service.FavourService;
import com.idoltrainer.webvideo.service.UserService;
import com.idoltrainer.webvideo.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 收藏接口
 *
 * @author zhangjieming
 * @time   2023/10/24
 */
@RestController
@RequestMapping("/favour")
@Slf4j
@Api("收藏模块")
public class FavourController {

    @Resource
    private FavourService favourService;

    @Resource
    private VideoService videoService;

    @Resource
    private UserService userService;

    /**
     * 收藏 / 取消收藏
     *
     * @param videoId  传入视频id
     * @param request
     * @return resultNum 收藏变化数
     */
    @GetMapping("/")
    @ApiOperation("收藏/取消收藏")
    public BaseResponse<Integer> doVideoFavour(@RequestParam Long videoId,
                                              HttpServletRequest request) {
        if (videoId == null || videoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能操作
        final User loginUser = userService.getLoginUser(request);
        int result = favourService.doPostFavour(videoId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我收藏的视频列表
     * @param request
     */
    @GetMapping("/my/list/page")
    @ApiOperation("获取我收藏的视频列表")
    public BaseResponse<List<Video>> listMyFavourVideoByPage(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        List<Favour> favoursIdList = favourService.query().eq("userId", loginUser.getId()).list();
        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        List<Video> videoList = new ArrayList<>();
        for (Favour favour:favoursIdList){
            //获取视频id
            Long videoId = favour.getVideoId();
            Video video = videoService.getById(videoId);
            videoList.add(video);
        }
        return ResultUtils.success(videoList);
    }

    /**
     * 获取用户收藏的帖子列表
     *
     * @param request
     */
    @GetMapping("/list/page")
    @ApiOperation(" 获取其他用户收藏的帖子列表")
    public BaseResponse<List<Video>> listFavourVideoByPage(@RequestParam Long userId, HttpServletRequest request) {
        List<Favour> favoursIdList = favourService.query().eq("userId", userId).list();
        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        List<Video> videoList = new ArrayList<>();
        for (Favour favour:favoursIdList){
            //获取视频id
            Long videoId = favour.getVideoId();
            Video video = videoService.getById(videoId);
            videoList.add(video);
        }
        return ResultUtils.success(videoList);
    }

}
