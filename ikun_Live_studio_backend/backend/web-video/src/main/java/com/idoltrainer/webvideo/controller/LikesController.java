package com.idoltrainer.webvideo.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idoltrainer.webvideo.common.BaseResponse;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.common.ResultUtils;
import com.idoltrainer.webvideo.domain.Likes;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.domain.Video;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.model.page.PageQuery;
import com.idoltrainer.webvideo.model.vo.video.VideoVO;
import com.idoltrainer.webvideo.service.LikesService;
import com.idoltrainer.webvideo.service.UserService;
import com.idoltrainer.webvideo.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * 点赞接口
 *
 * @author zhangjieming
 * @time   2023/10/24
 */
@RestController
@RequestMapping("/likes")
@Slf4j
@Api("点赞模块")
public class LikesController {
    @Resource
    private LikesService likesService;

    @Resource
    private UserService userService;

    @Resource
    private VideoService videoService;

    /**
     * 点赞 / 取消点赞
     *
     * @param id
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @GetMapping("/")
    @ApiOperation("点赞/取消点赞")
    public BaseResponse<Integer> doLikes(@RequestParam Long id,
                                         HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        long videoId = id;
        int result = likesService.doVideoLike(videoId, loginUser);
        return ResultUtils.success(result);
    }

    /**
     * 获取我点赞过的视频
     */
    @GetMapping("/liked_video")
    @ApiOperation("获取我点赞过的视频")
    public BaseResponse<List<Video>> queryLikesVideo(HttpServletRequest request) {
        // 登录才能点赞
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        //获取点赞对象
        List<Likes> likes = likesService.query().eq("userId", loginUser.getId()).list();
        //遍历对象
        List<Video> videoList = new ArrayList<>();
        for (Likes like:likes){
            //获取视频id
            Long videoId = like.getVideoId();
            Video video = videoService.getById(videoId);
            //添加
            videoList.add(video);

        }
        return ResultUtils.success(videoList);
    }

    /**
     * 给视频点赞过的人
     */


}