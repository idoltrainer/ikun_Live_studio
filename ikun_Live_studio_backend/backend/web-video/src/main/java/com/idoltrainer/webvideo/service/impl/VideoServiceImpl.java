package com.idoltrainer.webvideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.domain.*;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.mapper.FavourMapper;
import com.idoltrainer.webvideo.mapper.LikesMapper;
import com.idoltrainer.webvideo.mapper.VideoMapper;
import com.idoltrainer.webvideo.model.dto.video.VideoQueryDTO;
import com.idoltrainer.webvideo.model.vo.video.VideoVO;
import com.idoltrainer.webvideo.service.RelationsService;
import com.idoltrainer.webvideo.service.UserService;
import com.idoltrainer.webvideo.service.VideoService;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.intellij.lang.annotations.JdkConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService {

    @Resource
    private UserService userService;

    @Resource
    private LikesMapper likesMapper;

    @Resource
    private FavourMapper favourMapper;

    @Resource
    private RelationsService relationsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private VideoMapper videoMapper;

    @Override
    public void validVideo(Video video, boolean b) {
        if (video == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = video.getTitle();
        String content = video.getContent();
        String tags = video.getTags();
//        String coverUrl = video.getCoverUrl();
//        String playUrl = video.getPlayUrl();
        // 创建时，参数不能为空
        if (StringUtils.isAnyBlank(title, content, tags)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(title) && title.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "标题过长");
        }
        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    @Override
    public QueryWrapper<Video> getUserQueryWrapper(VideoQueryDTO videoQueryDTO) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        if (videoQueryDTO == null) {
            return queryWrapper;
        }
        String searchText = videoQueryDTO.getSearchText();
        //根据用户id查询
        queryWrapper.eq("userId",videoQueryDTO.getUserId());
        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("title", searchText).or().like("content", searchText);
        }
        queryWrapper.eq("isDelete", false);
        //根据时间进行降序
        queryWrapper.orderBy(true,false,"updateTime");
        return queryWrapper;
    }

    @Override
    public QueryWrapper<Video> getFavourQueryWrapper(VideoQueryDTO videoQueryDTO) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        if (videoQueryDTO == null) {
            return queryWrapper;
        }
        String searchText = videoQueryDTO.getSearchText();
        //根据用户id查询
        queryWrapper.eq("userId",videoQueryDTO.getUserId());
        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("title", searchText).or().like("content", searchText);
        }
        queryWrapper.eq("isDelete", false);
        //根据时间进行降序
        queryWrapper.orderBy(true,false,"updateTime");
        return queryWrapper;
    }

    @Override
    public List<Video> getPageAsList(Integer current, int size) {
        return videoMapper.getPageAsList(current,size) ;
    }


    @Override
    public QueryWrapper<Video> getQueryWrapper(VideoQueryDTO videoQueryDTO) {
        QueryWrapper<Video> queryWrapper = new QueryWrapper<>();
        if (videoQueryDTO == null) {
            return queryWrapper;
        }
        String searchText = videoQueryDTO.getSearchText();
        // 拼接查询条件
        if (StringUtils.isNotBlank(searchText)) {
            queryWrapper.like("title", searchText).or().like("content", searchText);
        }
        queryWrapper.eq("isDelete", false);
        //根据时间进行降序
        queryWrapper.orderBy(true,false,"updateTime");
        return queryWrapper;
    }

    @Override
    public Page<VideoVO> getVideoVOPage(Page<Video> videoPage, HttpServletRequest request) {
        List<Video> videoList = videoPage.getRecords();
        Page<VideoVO> videoVOPage = new Page<>(videoPage.getCurrent(), videoPage.getSize(), videoPage.getTotal());
        if (CollectionUtils.isEmpty(videoList)) {
            return videoVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = videoList.stream().map(Video::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> videoIdHasThumbMap = new HashMap<>();
        Map<Long, Boolean> videoIdHasFavourMap = new HashMap<>();
        User loginUser = userService.getLoginUserPermitNull(request);
        if (loginUser != null) {
            Set<Long> videoIdSet = videoList.stream().map(Video::getId).collect(Collectors.toSet());
            loginUser = userService.getLoginUser(request);
            // 获取点赞
            QueryWrapper<Likes> videoLikesQueryWrapper = new QueryWrapper<>();
            videoLikesQueryWrapper.in("videoId", videoIdSet);
            videoLikesQueryWrapper.eq("userId", loginUser.getId());
            List<Likes> postPostThumbList = likesMapper.selectList(videoLikesQueryWrapper);
            postPostThumbList.forEach(videoVideoLikes -> videoIdHasThumbMap.put(videoVideoLikes.getVideoId(), true));
            // 获取收藏
            QueryWrapper<Favour> videoFavourQueryWrapper = new QueryWrapper<>();
            videoFavourQueryWrapper.in("videoId", videoIdSet);
            videoFavourQueryWrapper.eq("userId", loginUser.getId());
            List<Favour> postFavourList = favourMapper.selectList(videoFavourQueryWrapper);
            postFavourList.forEach(postFavour -> videoIdHasFavourMap.put(postFavour.getVideoId(), true));
        }
        // 填充信息
        List<VideoVO> postVOList = videoList.stream().map(video -> {
            VideoVO videoVO = VideoVO.objToVo(video);
            Long userId = video.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            videoVO.setUserVO(userService.getUserVO(user));
            videoVO.setIsLike(videoIdHasThumbMap.getOrDefault(video.getId(), false));
            videoVO.setIsFavour(videoIdHasFavourMap.getOrDefault(video.getId(), false));
            return videoVO;
        }).collect(Collectors.toList());
        videoVOPage.setRecords(postVOList);
        return videoVOPage;
    }

    @Override
    public Page<Video> searchFromEs(VideoQueryDTO videoQueryDTO) {
        return null;
    }

    @Override
    public boolean sendFans(Long userId,Long videoId) {
        if(userId == null || userId < 0){
            return false;
        }
        if(videoId == null || videoId < 0){
            return false;
        }
        //获取粉丝
        List<Relations> relations = relationsService.query().eq("followId", userId).list();
        //遍历获取
        for (Relations relation:relations){
            //获取粉丝id
            Long followerId = relation.getFollowerId();
            //推送，每个用户维护一个zset，key是用户id
            String key = "feed:" + followerId;
            System.out.println(key);
            //键 值 时间戳
            stringRedisTemplate.opsForZSet().add(key,videoId.toString(),System.currentTimeMillis());
        }
        return true;
    }
}




