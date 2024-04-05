package com.beechannel.media.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beechannel.media.domain.po.PlayVideoList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author eotouch
 * @description 针对表【play_video_list】的数据库操作Mapper
 * @createDate 2023-12-11 15:13:49
 * @Entity com.beechannel.media.domain.po.PlayVideoList
 */
public interface PlayVideoListMapper extends BaseMapper<PlayVideoList> {

    List<PlayVideoList> getPlayVideoListByVideoId(
            @Param("videoId") Long videoId,
            @Param("userId") Long userId
    );
}




