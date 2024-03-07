package com.beechannel.media.controller;

import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.service.LikeListService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Description user's like action service
 * @Author eotouch
 * @Date 2024/02/14 20:06
 * @Version 1.0
 */
@RestController
@RequestMapping("/like")
public class LikeActionController {

    @Resource
    private LikeListService likeListService;

    @GetMapping
    public RestResponse getLikedVideoPage(PageParams pageParams){
        return likeListService.getLikedVideoPage(pageParams);
    }
}
