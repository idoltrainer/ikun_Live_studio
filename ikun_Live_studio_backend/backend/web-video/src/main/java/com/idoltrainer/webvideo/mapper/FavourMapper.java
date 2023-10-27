package com.idoltrainer.webvideo.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.idoltrainer.webvideo.domain.Favour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.idoltrainer.webvideo.domain.Video;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity generator.domain.Favour
 */
@Mapper
public interface FavourMapper extends BaseMapper<Favour> {

    Page<Video> listFavourPostByPage(IPage<Video> objectPage, QueryWrapper<Video> queryWrapper, Long id);
}




