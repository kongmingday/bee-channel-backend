package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.media.domain.dto.ParentCommentNode;
import com.beechannel.media.domain.po.Comment;
import com.beechannel.media.service.CommentService;
import com.beechannel.media.mapper.CommentMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author eotouch
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2023-12-03 22:06:07
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    private CommentMapper commentMapper;

    /**
     * @description get comment page by videoId
     * @param videoId target video id
     * @param pageParams page params
     * @return java.util.List<com.beechannel.media.domain.dto.ParentCommentNode>
     * @author eotouch
     * @date 2023-12-03 23:13
     */
    @Override
    public List<ParentCommentNode> getCommentPageByVideoId(Long videoId, PageParams pageParams) {
        Page<ParentCommentNode> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        commentMapper.getCommentPageByVideoId(page, videoId);
        return page.getRecords();
    }
}




