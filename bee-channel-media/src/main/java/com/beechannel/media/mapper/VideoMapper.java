package com.beechannel.media.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beechannel.media.domain.dto.AuditVideoItem;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.domain.po.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author eotouch
* @description 针对表【video】的数据库操作Mapper
* @createDate 2023-11-27 23:41:32
* @Entity com.beechannel.media.domain.po.Video
*/
public interface VideoMapper extends BaseMapper<Video> {

    SingleVideo getFullVideoInfo(@Param("videoId") Long videoId,
                                 @Param("currentUserId") Long currentUserId);

    IPage<AuditVideoItem> getPersonalVideoList(IPage<AuditVideoItem> page,
                                              @Param("currentUserId") Long currentUserId);
}




