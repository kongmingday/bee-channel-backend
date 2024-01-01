package com.beechannel.media.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.constant.VideoStatus;
import com.beechannel.media.domain.dto.FullUser;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.domain.po.Video;
import com.beechannel.media.feign.UserClient;
import com.beechannel.media.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description video service
 * @Author eotouch
 * @Date 2023/11/27 23:35
 * @Version 1.0
 */
@RestController
@RequestMapping("/video")
public class VideoController {

    @Resource
    private UserClient userClient;

    @Resource
    private VideoService videoService;

    @GetMapping("/{videoId}")
    public RestResponse getVideoInfo(@PathVariable Long videoId){
        return videoService.getVideoInfo(videoId);
    }

    @GetMapping
    public RestResponse getRecommendByCategory(Integer categoryId){
        LambdaQueryWrapper<Video> videoQuery = new LambdaQueryWrapper<>();
        videoQuery.eq(Video::getCategoryId, categoryId);
        videoQuery.eq(Video::getStatus, VideoStatus.APPROVE.getId());
        Page<Video> page = new Page<>(0, 6);
        List<Video> list = videoService.page(page, videoQuery).getRecords();

        List<SingleVideo> collect = list.stream().map(item -> {
            RestResponse<User> userInfo = userClient.getUserInfo(item.getAuthorId());
            if (userInfo.getCode() != 200) {
                return null;
            }
            User author = userInfo.getResult();
            FullUser concernUser = new FullUser();
            SingleVideo singleVideo = new SingleVideo();
            BeanUtils.copyProperties(item, singleVideo);
            BeanUtils.copyProperties(author, concernUser);
            singleVideo.setAuthor(concernUser);
            return singleVideo;
        }).collect(Collectors.toList());

        return RestResponse.success(collect);
    }

    @GetMapping("/personal")
    public RestResponse getPersonalVideoList(PageParams pageParams){
        return videoService.getPersonalVideoList(pageParams);
    }

}
