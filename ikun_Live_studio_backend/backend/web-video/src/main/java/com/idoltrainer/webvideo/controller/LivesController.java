package com.idoltrainer.webvideo.controller;


import com.idoltrainer.webvideo.common.BaseResponse;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.common.ResultUtils;
import com.idoltrainer.webvideo.domain.Lives;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.domain.Video;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.model.dto.video.VideoAddDTO;
import com.idoltrainer.webvideo.service.LivesService;
import com.idoltrainer.webvideo.service.UserService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static com.idoltrainer.webvideo.netty.AsyncTask.chatRoomsMap;

/**
 * 直播
 *
 * @author zhangjieming
 * @time   2023/10/24
 */
@RestController
@RequestMapping("/lives")
@Slf4j
@Api("直播模块")
public class LivesController {


    @Resource
    private UserService userService;

    @Resource
    private LivesService livesService;
    /**
     * 上传视频其他信息
     *
     * @param content
     * @param request
     * @return
     */
    @GetMapping("/add")
    @ApiOperation("创建直播")
    public BaseResponse<Long> addLive(@RequestParam String content, HttpServletRequest request) {
        //获取登录信息
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        //创建直播
        Lives lives = new Lives();
        lives.setAnchorId(loginUser.getId());
        lives.setVisitId("");
        lives.setCreateTime(new Date());
        lives.setUpdateTime(new Date());
        lives.setEndTime(new Date());
        lives.setTotalNum(0l);
        lives.setContent(content);
        livesService.save(lives);
        //开播
        ChannelGroup recipients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
        String key = "live:" + lives;
        chatRoomsMap.put(key,recipients);
        return ResultUtils.success(lives.getId());
    }

    @GetMapping("/close")
    @ApiOperation("关闭直播")
    public BaseResponse<Boolean> deleteVideo(Long liveId, HttpServletRequest request) {
        //获取登录信息
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //需要登录
        //关闭直播
        livesService.update()
                .eq("id", liveId)
                .setSql("isLive = 1")
                .update();
        boolean close = livesService.update()
                .eq("id", liveId)
                .set("createTime",new Date())
                .update();
        if (!close){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"关闭直播失败");
        }else{
            return ResultUtils.success(close);
        }
    }

    @GetMapping("/list")
    @ApiOperation("查询直播列表")
    public BaseResponse<List<Lives>> queryVideoList(HttpServletRequest request) {
        List<Lives> livesList = livesService.query().eq("isLive", 0).orderBy(true, false, "createTime").list();
        return ResultUtils.success(livesList);
    }

    @GetMapping("/detail")
    @ApiOperation("查询id查询直播")
    public BaseResponse<Lives> queryVideo(@RequestParam Long liveId,HttpServletRequest request) {
        Lives lives = livesService.getById(liveId);
        return ResultUtils.success(lives);
    }

}
