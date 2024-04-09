package com.beechannel.media.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.domain.po.PlayList;
import com.beechannel.media.domain.po.PlayVideoList;
import com.beechannel.media.service.PlayListService;
import com.beechannel.media.service.PlayVideoListService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description the play list service
 * @Author eotouch
 * @Date 2024/02/15 12:30
 * @Version 1.0
 */
@RestController
@RequestMapping("/playList")
public class PlayListController {

    @Resource
    private PlayListService playListService;

    @Resource
    private PlayVideoListService playVideoListService;

    @GetMapping
    public RestResponse getPlayList(@RequestParam(required = false) Integer isWatchLater){
        return playListService.getPlayListByCondition(isWatchLater);
    }


    @GetMapping("/{playListId}")
    public RestResponse getPlayListVideoPage(@PathVariable("playListId") Long playListId,  PageParams pageParams){
        return playListService.getPlayListVideoPage(playListId, pageParams);
    }

    @GetMapping("/later")
    public RestResponse getWatchLaterVideoPage(PageParams pageParams){
        return playListService.getWatchLaterVideoPage(pageParams);
    }

    @PostMapping
    public RestResponse newPlayList(String playListName){
        Long userId = SecurityUtil.getCurrentUserIdNotNull();

        PlayList playList = new PlayList();
        playList.setCreateTime(LocalDateTime.now());
        playList.setName(playListName);
        playList.setStatus(1);
        playList.setCreateUser(userId);
        boolean result = playListService.save(playList);
        return RestResponse.success(result);
    }

    @PutMapping("/{playListId}")
    public RestResponse updatePlayList(@PathVariable("playListId") Long playListId, @RequestBody PlayList playList){
        playList.setId(playListId);
        boolean flag = playListService.updateById(playList);
        return RestResponse.success(flag);
    }

    @DeleteMapping("/{playListId}")
    public RestResponse deletePlayList(@PathVariable("playListId") Long playListId){
        boolean flag = playListService.removeById(playListId);
        return RestResponse.success(flag);
    }

    @GetMapping("/inPlayList/{videoId}")
    public RestResponse getVideoInPlaylist(@PathVariable("videoId") Long videoId){
        List<PlayVideoList> result = playVideoListService.getVideoInPlaylist(videoId);
        return RestResponse.success(result);
    }

    @PostMapping("/batch/{videoId}")
    public RestResponse addVideoToPlayList(@RequestBody List<Long> playListIdList,
                                           @PathVariable("videoId") Long videoId){
        playVideoListService.addToPlayList(playListIdList, videoId);
        return RestResponse.success();
    }

    @DeleteMapping("/{playListId}/{videoId}")
    public RestResponse deleteVideoFromPlayList(@PathVariable("playListId") Long playListId,
                                           @PathVariable("videoId") Long videoId){

        LambdaQueryWrapper<PlayVideoList> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlayVideoList::getPlayListId, playListId);
        queryWrapper.eq(PlayVideoList::getVideoId, videoId);

        boolean flag = playVideoListService.remove(queryWrapper);

        return RestResponse.success(flag);
    }

    @PutMapping("/inPlayList/{videoId}")
    public RestResponse updateVideoInPlaylist(@PathVariable Long videoId, @RequestBody List<PlayVideoList> playVideoList){

        playVideoListService.updateVideoInPlaylist(videoId, playVideoList);

        return RestResponse.success();
    }
}
