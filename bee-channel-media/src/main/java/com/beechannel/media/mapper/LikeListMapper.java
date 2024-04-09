package com.beechannel.media.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beechannel.media.domain.po.LikeList;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author eotouch
* @description 针对表【like_list】的数据库操作Mapper
* @createDate 2023-12-06 17:26:38
* @Entity com.beechannel.media.domain.po.LikeList
*/
public interface LikeListMapper extends BaseMapper<LikeList> {

    Long deleteByParentCommentId(@Param("commentId") Long commentId);

    IPage<LikeList> getTopVideoId(IPage<LikeList> page);
}




