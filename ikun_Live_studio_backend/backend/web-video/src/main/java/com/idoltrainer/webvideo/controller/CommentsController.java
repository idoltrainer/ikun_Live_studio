package com.idoltrainer.webvideo.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idoltrainer.webvideo.common.BaseResponse;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.common.ResultUtils;
import com.idoltrainer.webvideo.domain.Comments;
import com.idoltrainer.webvideo.domain.Likes;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.domain.Video;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.model.dto.comments.CommentsQueryDTO;
import com.idoltrainer.webvideo.model.dto.video.VideoAddDTO;
import com.idoltrainer.webvideo.model.page.PageQuery;
import com.idoltrainer.webvideo.model.vo.comment.CommentVideoVO;
import com.idoltrainer.webvideo.service.CommentsService;
import com.idoltrainer.webvideo.service.UserService;
import com.idoltrainer.webvideo.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 收藏接口
 *
 * @author zhangjieming
 * @time   2023/10/24
 */
@RestController
@RequestMapping("/comments")
@Slf4j
@Api("评论模块")
public class CommentsController {

    @Resource
    private UserService userService;

    @Resource
    private CommentsService commentsService;

    @Resource
    private VideoService videoService;

    /**
     * 创建评论
     */
    @GetMapping("/add")
    @ApiOperation("创建评论")

    public BaseResponse<Long> addComments(@RequestParam String comment,@RequestParam Long videoId, HttpServletRequest request){
        //如果传入评论为空
        if (comment == null || comment.equals("")){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //判断是否登录
        User user = userService.getLoginUser(request);
        if(user == null){
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR,"登录之后才能评论");
        }
        //如果登录就创建评论
        Comments comments = new Comments();
        comments.setContent(comment);
        comments.setUserId(user.getId());
        comments.setVideoId(videoId);
        comments.setCreateTime(new Date());
        comments.setUpdateTime(new Date());
        commentsService.save(comments);
        return ResultUtils.success(comments.getId());
    }

    /**
     * 查看自己发布过的评论
     */
    @PostMapping("/my/list")
    @ApiOperation("查看自己发布过的评论")
    public BaseResponse<Page<Comments>> queryMyComment(@RequestBody PageQuery pageQuery, HttpServletRequest request){
        //判断是否登录
        User user = userService.getLoginUser(request);
        if(user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //取值
        long current = pageQuery.getCurrent();
        long pageSize = pageQuery.getPageSize();
        //如果登录就查询评论
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",user.getId());
        Page<Comments> commentsPage = commentsService.page(new Page<>(current, pageSize),
                queryWrapper);
        return ResultUtils.success(commentsPage);
    }
    
    
    /**
     *
     * 查找一个视频中所有评论
     */
    @PostMapping("/video/list")
    @ApiOperation("查找一个视频中所有评论")
    public BaseResponse<Page<Comments>> queryVideoComment(@RequestBody CommentsQueryDTO commentsQueryDTO, HttpServletRequest request){
        
        //取值
        long current = commentsQueryDTO.getCurrent();
        long pageSize = commentsQueryDTO.getPageSize();
        Long videoId = commentsQueryDTO.getVideoId();
        //如果登录就查询评论
        QueryWrapper<Comments> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("videoId",videoId);
        Page<Comments> commentsPage = commentsService.page(new Page<>(current, pageSize),
                queryWrapper);
        return ResultUtils.success(commentsPage);
    }

    /**
     *
     * 删除自己评论
     */

    @GetMapping("/my/delete")
    @ApiOperation("删除自己的评论（登录后才可调用）")
    public BaseResponse<Boolean> deleteMyComment(Long commentId,HttpServletRequest request){

        //判断是否登录
        User user = userService.getLoginUser(request);
        if(user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        //如果登录就查询评论
        boolean b = commentsService.removeById(commentId);
        return ResultUtils.success(b);
    }

    /**
     * 获取我评论过的视频
     */
    @GetMapping("/commented_video")
    @ApiOperation("获取我评论过的评论和视频id")
    public BaseResponse<List<CommentVideoVO>> queryCommentsVideo(HttpServletRequest request) {
        // 登录才能点赞
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        //获取点赞对象
        List<Comments> commentsList = commentsService.query().eq("userId", loginUser.getId()).list();
        //创建返回对象
        List<CommentVideoVO> commentVideoVOList = new ArrayList<>();
        //遍历对象
        for (Comments comments:commentsList){
            //获取视频id
            Long videoId = comments.getVideoId();
            //获取评论
            CommentVideoVO commentVideoVO = new CommentVideoVO();
            commentVideoVO.setCommentContent(comments.getContent());
            commentVideoVO.setCreateTime(comments.getCreateTime());
            commentVideoVO.setUpdateTime(comments.getUpdateTime());
            commentVideoVO.setVideoId(videoId);
            //添加
            commentVideoVOList.add(commentVideoVO);
        }
        return ResultUtils.success(commentVideoVOList);
    }

    /**
     * 给视频评论过的人（暂不实现）
     */
}
