package com.idoltrainer.webvideo.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.idoltrainer.webvideo.common.BaseResponse;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.common.ResultUtils;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.model.dto.user.*;
import com.idoltrainer.webvideo.model.vo.user.UserLoginVO;
import com.idoltrainer.webvideo.model.vo.user.UserVO;
import com.idoltrainer.webvideo.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


/**
 * 用户接口
 *
 * @author zhangjieming
 * @time   2023/10/24
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api("用户模块")
public class UserController {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "IKUN";


    @Autowired
    private UserService userService;


    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册封装对象
     * @return BaseResponse<Long>
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterDTO userRegisterDTO) {
        //判断前端传来的实体对象是否为空
        if (userRegisterDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //获取封装类参数
        String userAccount = userRegisterDTO.getUserAccount();
        String userPassword = userRegisterDTO.getUserPassword();
        String checkPassword = userRegisterDTO.getCheckPassword();
        //如果有一个空就返回参数错误
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginDTO
     * @param request
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public BaseResponse<UserLoginVO> userLogin(@RequestBody UserLoginDTO userLoginDTO, HttpServletRequest request) {
        if (userLoginDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginDTO.getUserAccount();
        String userPassword = userLoginDTO.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserLoginVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }


    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("用户注销")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    @ApiOperation("获取当前用户登录，用于前端实现页面权限管理")
    public BaseResponse<UserLoginVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    /**
     * 创建用户(如果需要管理员的话，可以调用这个接口，用来管理用户)
     *
     * @param userAddDTO
     * @param request
     * @return
     */
    @PostMapping("/add")
    @ApiOperation("创建用户（管理员用，如果暂时不要管理员可以不调用这个接口）")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddDTO userAddDTO, HttpServletRequest request) {
        if (userAddDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //新建用户
        User user = new User();
        String userAccount = userAddDTO.getUserAccount();
        String userPassword = userAddDTO.getUserPassword();
        //判断条件
        if (userAccount.length() < 4 || userAccount.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误,账号必须大于4位小于20位");
        }
        if (userPassword.length() < 8 || userAccount.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误,密码必须大于8位小于20位");
        }
        //密码加密
        // 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        BeanUtils.copyProperties(userAddDTO, user);
        user.setUserContent("这个人很懒，什么也没留下");
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 删除用户(如果需要管理员的话，可以调用这个接口，用来删除用户)
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/delete")
    @ApiOperation("删除用户（管理员用，如果暂时不要管理员可以不调用这个接口）")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestParam Long id, HttpServletRequest request) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //根据id删除字段
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新用户 (管理员用，如果暂时不要管理员可以不调用这个接口)
     *
     * @param userUpdateDTO
     * @param request
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("更新用户 (管理员用，如果暂时不要管理员可以不调用这个接口)")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateDTO userUpdateDTO,
            HttpServletRequest request) {
        if (userUpdateDTO == null || userUpdateDTO.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        boolean result = userService.updateById(user);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户 （管理员获取用户）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @ApiOperation("根据id获取用户（管理员用，如果暂时不要管理员可以不调用这个接口）")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
//        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        //用户脱敏
        user.setUserPassword(null);
        user.setIsDelete(null);
        user.setCreateTime(null);
        user.setUpdateTime(null);
        return ResultUtils.success(user);
    }

    /**
     * 分页获取用户列表（管理员分页获取用户列表）
     *
     * @param userPageQueryDTO
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @ApiOperation("分页获取用户列表（管理员用，如果暂时不要管理员可以不调用这个接口）")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserPageQueryDTO userPageQueryDTO,
            HttpServletRequest request) {
        long current = userPageQueryDTO.getCurrent();
        long size = userPageQueryDTO.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userPageQueryDTO));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表（用户分页获取用户vo对象）
     *
     * @param userSearchDTO
     * @param request
     * @return
     */
    @ApiOperation("分页查询其他用户")
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserSearchDTO userSearchDTO,
                                                       HttpServletRequest request) {
        if (userSearchDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userSearchDTO.getCurrent();
        long size = userSearchDTO.getPageSize();
        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getSearchQueryWrapper(userSearchDTO));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 更新个人信息（用户更新自己的个人信息）
     *
     * @param userUpdateDTO
     * @param request
     * @return
     */
    @PostMapping("/update/content")
    @ApiOperation("更新我的用户信息")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateDTO userUpdateDTO,
            HttpServletRequest request) {
        if (userUpdateDTO == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
//        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

}
