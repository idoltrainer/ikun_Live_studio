package com.idoltrainer.webvideo.controller;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.soap.Addressing;

import com.idoltrainer.webvideo.common.BaseResponse;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.common.ResultUtils;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.enums.FileUploadType;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.model.vo.file.FileVO;
import com.idoltrainer.webvideo.service.UserService;
import com.idoltrainer.webvideo.util.KodoUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/file")
@Slf4j
@Api("文件上传模块")
public class FileController {

    @Resource
    private UserService userService;

    @Autowired
    private KodoUtils kodoUtils;


    private final String DOMAIN = "http://s3159m8av.hn-bkt.clouddn.com/";

    private final String STORESPACE = "user_file_store";
    /**
     * 文件上传
     *
     * @param multipartFile
     * @param request
     * @return
     */
    @PostMapping("/upload/avatar")
    @ApiOperation("上传头像")
    public BaseResponse<String> uploadAvatar(@RequestPart("file") MultipartFile multipartFile,
                                            HttpServletRequest request) {

        validAvatar(multipartFile, "user_avatar");
        User loginUser = userService.getLoginUser(request);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format(STORESPACE+"/%s/%s/%s","user_avatar", loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件到七牛云
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
//            cosManager.putObject(filepath, file);
            System.out.println(filepath);
            System.out.println(file);
            System.out.println(DOMAIN + filepath);
            kodoUtils.upload(file,filepath);
            // 返回可访问地址
            return ResultUtils.success(DOMAIN + filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 视频上传
     *
     * @param multipartFile
     * @param request
     * @return
     */
    @ApiOperation("上传视频")
    @PostMapping("/upload/video")
    public BaseResponse<String> uploadVideo(@RequestPart("file") MultipartFile multipartFile,
                                            HttpServletRequest request) {

        validVideo(multipartFile, "user_video");
        User loginUser = userService.getLoginUser(request);
        // 文件目录：根据业务、用户来划分
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String filename = uuid + "-" + multipartFile.getOriginalFilename();
        String filepath = String.format(STORESPACE+"/%s/%s/%s","user_video", loginUser.getId(), filename);
        File file = null;
        try {
            // 上传文件到七牛云
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);
//            cosManager.putObject(filepath, file);
            System.out.println(filepath);
            System.out.println(file);
            System.out.println(DOMAIN + filepath);
            kodoUtils.upload(file,filepath);
            // 返回可访问地址
            FileVO fileVO = new FileVO();
            fileVO.setPlayUrl(DOMAIN + filepath);
            fileVO.setCoverUrl(DOMAIN + filepath+"?vframe/jpg/offset/0");
            return ResultUtils.success(DOMAIN + filepath);
        } catch (Exception e) {
            log.error("file upload error, filepath = " + filepath, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            if (file != null) {
                // 删除临时文件
                boolean delete = file.delete();
                if (!delete) {
                    log.error("file delete error, filepath = {}", filepath);
                }
            }
        }
    }

    /**
     * 校验头像
     *
     * @param multipartFile
     * @param fileUploadType 业务类型
     */
    private void validAvatar(MultipartFile multipartFile, String fileUploadType) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024L;
        //如果等于
        if (fileSize > ONE_M) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1M");
        }
        if (!Arrays.asList("jpeg", "jpg", "svg", "png", "webp").contains(fileSuffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
        }
    }

    /**
     * 校验视频
     *
     * @param multipartFile
     * @param fileUploadType 业务类型
     */
    private void validVideo(MultipartFile multipartFile, String fileUploadType) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 1024 * 1024 * 1024L;
        //如果等于
        if (fileSize > ONE_M) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 1G");
        }
        if (!Arrays.asList("mp4", "flv", "avi", "mkv", "mov","m3u8","WebM","wmv").contains(fileSuffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
        }
    }
}
