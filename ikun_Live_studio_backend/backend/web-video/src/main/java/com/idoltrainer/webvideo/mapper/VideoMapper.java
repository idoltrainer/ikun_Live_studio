package com.idoltrainer.webvideo.mapper;

import com.idoltrainer.webvideo.domain.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Entity generator.domain.Video
 */
@Mapper
public interface VideoMapper extends BaseMapper<Video> {

    @Select("select * from video order by createTime desc limit ${current},${size}")
    List<Video> getPageAsList(@Param("current") Integer current,@Param("size") int size);
}




