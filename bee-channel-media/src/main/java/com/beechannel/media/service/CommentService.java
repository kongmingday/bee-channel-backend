package com.beechannel.media.service;

import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.dto.CommitCommentParam;
import com.beechannel.media.domain.po.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author eotouch
* @description 针对表【comment】的数据库操作Service
* @createDate 2023-12-03 22:06:07
*/
public interface CommentService extends IService<Comment> {

    /**
     * @description get comment page by videoId
     * @param videoId target video id
     * @param orderBy sort
     * @param pageParams page params
     * @return RestResponse
     * @author eotouch
     * @date 2023-12-03 23:13
     */
    RestResponse getCommentPageByVideoId(Long videoId, Integer orderBy, PageParams pageParams);

    /**
     * @description get children comment by the parent's id
     * @param parentId the parent's id of the comment
     * @param pageParams page params
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-12-05 15:48
     */
    RestResponse getChildrenCommentByParentId(Long parentId, PageParams pageParams);

    /**
     * @description delete comment
     * @param commentId the comment's id
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-12-10 18:09
     */
    RestResponse deleteComment(Long commentId);

    /**
     * @description commit comment
     * @param param
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-12-10 18:10
     */
    RestResponse commitComment(CommitCommentParam param);
}
