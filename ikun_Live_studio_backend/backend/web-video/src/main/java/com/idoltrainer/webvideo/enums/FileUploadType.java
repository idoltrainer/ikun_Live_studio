package com.idoltrainer.webvideo.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;



import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;

/**
 * 文件上传业务类型枚举
 *
 */
public enum  FileUploadType {

    USER_AVATAR("user_avatar", "user_avatar"),
    USER_VIDEO("user_video","user_video");

    private final String text;

    private final String value;

    FileUploadType(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static FileUploadType getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (FileUploadType type : FileUploadType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
