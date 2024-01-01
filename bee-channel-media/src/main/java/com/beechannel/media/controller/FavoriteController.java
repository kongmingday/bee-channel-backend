package com.beechannel.media.controller;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.dto.FavoriteActionParam;
import com.beechannel.media.mapper.LikeListMapper;
import com.beechannel.media.service.LikeListService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description favorite service
 * @Author eotouch
 * @Date 2023/12/08 16:31
 * @Version 1.0
 */
@RestController
@RequestMapping("/favorite")
public class FavoriteController {

    @Resource
    private LikeListService likeListService;

    @PostMapping("/change")
    public RestResponse favoriteChange(@Valid @RequestBody FavoriteActionParam param){
        return likeListService.favoriteChange(param);
    }
}
