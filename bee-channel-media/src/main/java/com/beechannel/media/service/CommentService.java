package com.beechannel.media.service;

import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.media.domain.dto.ParentCommentNode;
import com.beechannel.media.domain.po.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author eotouch
* @description 针对表【comment】的数据库操作Service
* @createDate 2023-12-03 22:06:07
*/
public interface CommentService extends IService<Comment> {

    /**
     * @description get comment page by videoId
     * @param videoId target video id
     * @param pageParams page params
     * @return java.util.List<com.beechannel.media.domain.dto.ParentCommentNode>
     * @author eotouch
     * @date 2023-12-03 23:13
     */
    List<ParentCommentNode> getCommentPageByVideoId(Long videoId, PageParams pageParams);
}
