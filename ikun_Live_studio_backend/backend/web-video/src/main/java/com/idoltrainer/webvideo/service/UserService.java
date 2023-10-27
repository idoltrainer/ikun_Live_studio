package com.idoltrainer.webvideo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idoltrainer.webvideo.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.idoltrainer.webvideo.model.dto.user.UserLoginDTO;
import com.idoltrainer.webvideo.model.dto.user.UserPageQueryDTO;
import com.idoltrainer.webvideo.model.dto.user.UserSearchDTO;
import com.idoltrainer.webvideo.model.vo.user.UserLoginVO;
import com.idoltrainer.webvideo.model.vo.user.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface UserService extends IService<User> {

    long userRegister(String userAccount, String userPassword, String checkPassword);

    UserLoginVO userLogin(String userAccount, String userPassword, HttpServletRequest request);

    boolean userLogout(HttpServletRequest request);

    User getLoginUser(HttpServletRequest request);

    UserLoginVO getLoginUserVO(User user);

    List<UserVO> getUserVO(List<User> records);

    UserVO getUserVO(User user);

    QueryWrapper<User> getQueryWrapper(UserPageQueryDTO userPageQueryDTO);

    User getLoginUserPermitNull(HttpServletRequest request);

    QueryWrapper<User> getSearchQueryWrapper(UserSearchDTO userSearchDTO);
}
