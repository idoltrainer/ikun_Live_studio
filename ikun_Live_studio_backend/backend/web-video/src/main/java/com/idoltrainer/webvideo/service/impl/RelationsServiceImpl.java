package com.idoltrainer.webvideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.domain.Likes;
import com.idoltrainer.webvideo.domain.Relations;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.mapper.RelationsMapper;
import com.idoltrainer.webvideo.service.RelationsService;
import com.idoltrainer.webvideo.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service
public class RelationsServiceImpl extends ServiceImpl<RelationsMapper, Relations>
    implements RelationsService {

    @Resource
    private UserService userService;

    @Override
    public int focus(Long userId, User loginUser) {
        //创建新的关系
        Relations relations = new Relations();
        relations.setFollowerId(loginUser.getId());
        relations.setFollowId(userId);
        QueryWrapper<Relations> relationsQueryWrapper = new QueryWrapper<>(relations);
        Relations oldRelations = this.getOne(relationsQueryWrapper);
        boolean result;
        // 已点赞
        if (oldRelations != null) {
            //数据少一条
            result = this.remove(relationsQueryWrapper);
            if (result) {
                // 关注数 - 1
                //取关，关注的人粉丝数-1
                userService.update()
                        .eq("id", userId)
                        .gt("followerNum", 0)
                        .setSql("followerNum = followerNum - 1")
                        .update();
                //我的关注-1
                result = userService.update()
                        .eq("id", loginUser.getId())
                        .gt("followNum", 0)
                        .setSql("followNum = followNum - 1")
                        .update();
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未关注
            result = this.save(relations);
            if (result) {
                // 关注数 + 1
                userService.update()
                        .eq("id", userId)
                        .setSql("followerNum = followerNum + 1")
                        .update();
                result = userService.update()
                        .eq("id", loginUser.getId())
                        .setSql("followNum = followNum + 1")
                        .update();
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}




