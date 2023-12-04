package com.beechannel.media.controller;

import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.dto.ParentCommentNode;
import com.beechannel.media.service.CommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
    public RestResponse getCommentPage(@PathVariable Long videoId, PageParams pageParams){
        List<ParentCommentNode> result = commentService.getCommentPageByVideoId(videoId, pageParams);
        return RestResponse.success(result);
    }
}
