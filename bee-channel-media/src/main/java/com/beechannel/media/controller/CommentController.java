package com.beechannel.media.controller;

import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.dto.CommitCommentParam;
import com.beechannel.media.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Description comment service
 * @Author eotouch
 * @Date 2023/12/03 22:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Resource
    private CommentService commentService;

    @GetMapping("/{videoId}")
    public RestResponse getCommentPage(@PathVariable Long videoId,
                                       @RequestParam(value = "orderBy", required = false) Integer orderBy,
                                       PageParams pageParams){
        return commentService.getCommentPageByVideoId(videoId, orderBy, pageParams);
    }

    @GetMapping("/children/{parentId}")
    public RestResponse getChildrenComment(@PathVariable Long parentId,PageParams pageParams){
        return commentService.getChildrenCommentByParentId(parentId, pageParams);
    }

    @PostMapping
    public RestResponse commitComment(@RequestBody CommitCommentParam param) {
        return commentService.commitComment(param);
    }

    @DeleteMapping("/{commentId}")
    public RestResponse deleteComment(@PathVariable Long commentId){
        return commentService.deleteComment(commentId);
    }
}
