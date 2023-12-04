package com.beechannel.media.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beechannel.media.domain.dto.ParentCommentNode;
import com.beechannel.media.domain.po.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author eotouch
* @description 针对表【comment】的数据库操作Mapper
* @createDate 2023-12-03 22:06:07
* @Entity com.beechannel.media.domain.po.Comment
*/
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<ParentCommentNode> getCommentPageByVideoId(Page<ParentCommentNode> page,
                                                     @Param("videoId") Long videoId);

}




