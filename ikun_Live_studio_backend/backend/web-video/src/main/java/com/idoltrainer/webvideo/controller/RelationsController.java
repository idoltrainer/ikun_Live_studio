package com.idoltrainer.webvideo.controller;


import com.idoltrainer.webvideo.common.BaseResponse;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.common.ResultUtils;
import com.idoltrainer.webvideo.domain.Likes;
import com.idoltrainer.webvideo.domain.Relations;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.domain.Video;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.model.page.PageQuery;
import com.idoltrainer.webvideo.service.RelationsService;
import com.idoltrainer.webvideo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户关系接口
 *
 * @author zhangjieming
 * @time   2023/10/24
 */
@RestController
@RequestMapping("/relations")
@Slf4j
@Api("用户关系模块")
public class RelationsController {

    @Resource
    private UserService userService;

    @Resource
    private RelationsService relationsService;

    /**
     * 查询关注列表
     */
    @GetMapping("/follow")
    @ApiOperation("查询我的关注列表")
    public BaseResponse<List<User>> queryFollow(HttpServletRequest request) {
        // 登录才能点赞
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        //获取点赞对象
        List<Relations> relations = relationsService.query().eq("followerId", loginUser.getId()).list();
        //遍历对象
        List<User> userList = new ArrayList<>();
        for (Relations relation:relations){
            //获取视频id
            Long followId = relation.getFollowId();
            User user = userService.getById(followId);
            user.setUserPassword(null);
            user.setIsDelete(null);
            //添加
            userList.add(user);

        }
        return ResultUtils.success(userList);
    }

    /**
     * 查询粉丝列表
     */
    @GetMapping("/follower")
    @ApiOperation("查询我的粉丝列表")
    public BaseResponse<List<User>> queryFollower(HttpServletRequest request) {
        // 登录才能点赞
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        //获取点赞对象
        List<Relations> relations = relationsService.query().eq("followId", loginUser.getId()).list();
        //遍历对象
        List<User> userList = new ArrayList<>();
        for (Relations relation:relations){
            //获取视频id
            Long followerId = relation.getFollowerId();
            User user = userService.getById(followerId);
            user.setUserPassword(null);
            user.setIsDelete(null);
            //添加
            userList.add(user);
        }
        return ResultUtils.success(userList);
    }

    /**
     * 查询附近的人(暂不实现)
     */

    /**
     * 查询共同好友（暂不实现）
     */

    /**
     * 关注和取关
     * 传入要关注的人的id
     */
    @GetMapping("/focus")
    @ApiOperation("关注/取关")
    public BaseResponse<Integer> focus(@RequestParam Long userId,
                                         HttpServletRequest request) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能关注
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        int result = relationsService.focus(userId, loginUser);
        return ResultUtils.success(result);
    }


}
