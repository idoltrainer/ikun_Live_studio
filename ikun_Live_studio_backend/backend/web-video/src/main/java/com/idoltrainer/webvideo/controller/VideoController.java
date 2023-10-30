package com.idoltrainer.webvideo.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.idoltrainer.webvideo.common.BaseResponse;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.common.ResultUtils;
import com.idoltrainer.webvideo.domain.*;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.model.dto.video.VideoAddDTO;
import com.idoltrainer.webvideo.model.dto.video.VideoQueryDTO;
import com.idoltrainer.webvideo.model.vo.video.VideoVO;
import com.idoltrainer.webvideo.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 视频接口
 *
 * @author zhangjieming
 * @time   2023/10/24
 */
@RestController
@RequestMapping("/video")
@Slf4j
@Api("视频模块")
public class VideoController {
    @Resource
    private VideoService videoService;

    @Resource
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private FavourService favourService;

    @Resource
    private LikesService likesService;


    private final static Gson GSON = new Gson();

    /**
     * 上传视频其他信息
     *
     * @param videoAddDTO
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("发布新视频")
    public BaseResponse<Long> addVideo(@RequestBody VideoAddDTO videoAddDTO, HttpServletRequest request) {
        if (videoAddDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Video video = new Video();
        BeanUtils.copyProperties(videoAddDTO, video);
        List<String> tags = videoAddDTO.getTags();
        if (tags != null) {
            video.setTags(GSON.toJson(tags));
        }
        videoService.validVideo(video, true);
        User loginUser = userService.getLoginUser(request);
        video.setUserId(loginUser.getId());
        video.setFavourNum(0l);
        video.setLikesNum(0l);
        video.setCreateTime(new Date());
        video.setUpdateTime(new Date());
        boolean result = videoService.save(video);
        //发送给粉丝使用redis的zset
        boolean b = videoService.sendFans(loginUser.getId(), video.getId());
        if (!b){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"推荐给关注用户异常！");
        }
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newPostId = video.getId();
        return ResultUtils.success(newPostId);
    }

    /**
     * 删除
     *
     * @param videoId
     * @param request
     * @return
     */
    @GetMapping("/delete")
    @ApiOperation("删除视频")
    public BaseResponse<Boolean> deleteVideo(@RequestParam Long videoId, HttpServletRequest request) {
        if (videoId == null || videoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //获取登录对象
        User user = userService.getLoginUser(request);
        // 判断是否存在
        Video oldVideo = videoService.getById(videoId);
//        ThrowUtils.throwIf(oldPost == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
//        if (!oldPost.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
//            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
//        }
        //仅本人可以删除
        if (!oldVideo.getUserId().equals(user.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = videoService.removeById(videoId);
        return ResultUtils.success(b);
    }

    /**
     * 分页获取当前用户创建的资源列表
     *
     * @param videoQueryDTO
     * @param request
     * @return
     */
    @PostMapping("/my/list/page/vo")
    @ApiOperation("分页获取我发布过的视频列表")
    public BaseResponse<Page<VideoVO>> listMyVideoVOByPage(@RequestBody VideoQueryDTO videoQueryDTO,
                                                          HttpServletRequest request) {
        //如果为空
        if (videoQueryDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        videoQueryDTO.setUserId(loginUser.getId());
        Long current = videoQueryDTO.getCurrent();
        Long size = videoQueryDTO.getPageSize();
        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Video> postPage = videoService.page(new Page<>(current, size),
                videoService.getUserQueryWrapper(videoQueryDTO));
        return ResultUtils.success(videoService.getVideoVOPage(postPage, request));
    }

    /**
     * 分页获取其他用户创建的资源列表
     *
     * @param videoQueryDTO
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    @ApiOperation("分页其他用户发布过的视频列表")
    public BaseResponse<Page<VideoVO>> listVideoVOByPage(@RequestBody VideoQueryDTO videoQueryDTO,
                                                           HttpServletRequest request) {
        //如果为空
        if (videoQueryDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = videoQueryDTO.getCurrent();
        long size = videoQueryDTO.getPageSize();
        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Video> postPage = videoService.page(new Page<>(current, size),
                videoService.getUserQueryWrapper(videoQueryDTO));
        return ResultUtils.success(videoService.getVideoVOPage(postPage, request));
    }

    /**
     * 分页搜索（查询，封装类,先不用es）
     *
     * @param videoQueryDTO
     * @param request
     * @return
     */
    @PostMapping("/search/page/vo")
    @ApiOperation("搜索视频")
    public BaseResponse<Page<VideoVO>> searchVideoVOByPage(@RequestBody VideoQueryDTO videoQueryDTO,
                                                         HttpServletRequest request) {
        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//        Page<Video> postPage = videoService.searchFromEs(videoQueryDTO);
        long current = videoQueryDTO.getCurrent();
        long size = videoQueryDTO.getPageSize();
        Page<Video> postPage = videoService.page(new Page<>(current, size),
                videoService.getQueryWrapper(videoQueryDTO));
        return ResultUtils.success(videoService.getVideoVOPage(postPage, request));
    }

    /**
     *
     * 根据id获取视频详情页
     * @param request
     * @return
     */
    @GetMapping("/details")
    @ApiOperation("根据id获取视频详情页")
    public BaseResponse<VideoVO> getDetailById(@RequestParam Long videoId,HttpServletRequest request) {
        if(videoId == null || videoId < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Video video = videoService.getById(videoId);
        VideoVO videoVO = new VideoVO();
        //返回更详细信息
        BeanUtil.copyProperties(video,videoVO);
        //还要查
        return ResultUtils.success(videoVO);
    }

    /**
     *
     * feed流自动投喂,使用redis进行滚动分页，不然会出现重复的视频！
     * @param lastId
     * @return
     * 传入lastId,第一次是当前时间戳，第二次是后端返回结果
     * 传入offset 第一次是0，之后都是1
     * 传入size 一般为3
     */
    @GetMapping("/feed")
    @ApiOperation("feed流实现投喂，每次查3条;注意：lastId时间戳的单位是毫秒数，不然查不到！！！")
    public BaseResponse<Page<VideoVO>> feedStream(@RequestParam Long lastId,@RequestParam(defaultValue = "0") Integer offSet,@RequestParam(defaultValue = "0")Integer current,HttpServletRequest request) {
        //查询收件箱
        User loginUser = userService.getLoginUser(request);
        //每次查3条
        int count = 3;
        //判断情况
        if (loginUser == null){
            //未登录，可以查询，我们先从数据库中查询
            QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
            videoQueryWrapper.orderBy(true,false,"createTime");
            Page<Video> videoPage = videoService.page(new Page<>(current, count),videoQueryWrapper);
            Page<VideoVO> videoVOPage = videoService.getVideoVOPage(videoPage, request);
            return ResultUtils.success(videoVOPage);
        }else{
            //声明一个page
            Page<VideoVO> videoVOPage = null;
            //已登录，可以按照feed流机制推荐
            String key = "feed:" + loginUser.getId();
            System.out.println(key);
            //feed流
            Set<ZSetOperations.TypedTuple<String>> sets = stringRedisTemplate.opsForZSet().reverseRangeByScoreWithScores(key, 0, lastId, offSet, count);

            //查询结果如果就是3条，直接返回就可以
            List<Long> videoIdList = new ArrayList<>(sets.size());
            Long minTime = 0l;
            int os = 1;
            //这一段滚动数组查询有点像贪心
            System.out.println("set的尺寸："+sets.size());
            for (ZSetOperations.TypedTuple<String> set: sets){
                System.out.println("我们从redis读取值："+set);
                //获取视频id
                String videoId = set.getValue();
                videoIdList.add(Long.valueOf(videoId));
                //获取分数
                Long time = set.getScore().longValue();
                //比较
                if (time == minTime){
                    os++;
                }else{
                    minTime = time;
                    os = 1;
                }
            }
            List<Video> videoList = new ArrayList<>(sets.size());
            for(Long videoId : videoIdList){
                Video video = videoService.getById(videoId);
                videoList.add(video);
            }
            //视频列表
//                for(Long videoId : videoIdList){
//                    Video video = videoService.getById(videoId);
//                    VideoVO videoVO = new VideoVO();
//                    BeanUtil.copyProperties(video,videoVO);
//                    //查询关系表。是否点赞过，收藏过
//                    QueryWrapper<Favour> queryWrapper = new QueryWrapper<>();
//                    queryWrapper.eq("videoId",videoId).eq("userId",loginUser.getId());
//                    Favour favour = favourService.getOne(queryWrapper);
//                    videoVO.setIsLike(favour != null);
//                    //是否收藏过
//                    QueryWrapper<Likes> queryWrapper2 = new QueryWrapper<>();
//                    queryWrapper2.eq("videoId",videoId).eq("userId",loginUser.getId());
//                    Likes likes = likesService.getOne(queryWrapper2);
//                    videoVO.setIsLike(likes != null);
//                    //装入集合
//                    videoVOList.add(videoVO);
//                }


            //如果不满3条，从数据库查询
            if (videoList.size() < count){
                //查询条数
                int size = count - sets.size();
//                QueryWrapper<Video> videoQueryWrapper = new QueryWrapper<>();
//                videoQueryWrapper.orderBy(true,false,"createTime");
//                Page<Video> videoPage = videoService.page(new Page<>(current, size),videoQueryWrapper);
                List<Video> videoList1 = videoService.getPageAsList(current,size);
                for (Video video:videoList1){
                    videoList.add(video);
                }
//                videoVOPage = videoService.getVideoVOPage(videoPage, request);

            }
            Page<Video> videoPage = new Page(0,videoList.size());
            videoPage.setRecords(videoList);
            videoPage.setTotal(videoIdList.size());
            videoVOPage = videoService.getVideoVOPage(videoPage, request);
            return ResultUtils.success(videoVOPage,minTime.toString());
        }
    }


    /**
     * 搜索周边人发布的视频
     */



    /**
     * 根据喜欢的标签推送，你可能喜欢
     */


    /**
     * 查询观看历史
     */


}
