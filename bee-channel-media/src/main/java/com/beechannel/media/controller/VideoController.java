package com.beechannel.media.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beechannel.base.constant.AuditStatus;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.PageResult;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.domain.vo.SearchParams;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.domain.po.Video;
import com.beechannel.media.feign.UserClient;
import com.beechannel.media.service.RecommendService;
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

    @Resource
    private RecommendService recommendService;

    @GetMapping("/{videoId}")
    public RestResponse getVideoInfo(@PathVariable Long videoId) {
        return videoService.getVideoInfo(videoId);
    }

    @GetMapping("/page")
    public RestResponse searchVideoPage(SearchParams searchParams) {
        return videoService.searchVideo(searchParams);
    }

    @GetMapping
    public RestResponse getVideoCategory(Integer categoryId, PageParams pageParams) {
        LambdaQueryWrapper<Video> videoQuery = new LambdaQueryWrapper<>();
        videoQuery.eq(Video::getCategoryId, categoryId);
        videoQuery.eq(Video::getStatus, AuditStatus.APPROVE.getId());
        Page<Video> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
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
    public RestResponse getPersonalVideoList(PageParams pageParams) {
        return videoService.getPersonalVideoList(pageParams);
    }

    @GetMapping("/personal/{authorId}")
    public RestResponse getAuthorVideoList(@PathVariable Long authorId, PageParams pageParams) {
        // get author's video list
        LambdaQueryWrapper<Video> videoQuery = new LambdaQueryWrapper<>();
        videoQuery.eq(Video::getAuthorId, authorId);
        videoQuery.eq(Video::getStatus, AuditStatus.APPROVE.getId());
        IPage<Video> pageInfo = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        videoService.page(pageInfo, videoQuery);

        PageResult<List<Video>> pageResult = new PageResult<>();
        pageResult.setData(pageInfo.getRecords());
        pageResult.setTotal((int) pageInfo.getTotal());
        return RestResponse.success(pageResult);
    }

    @GetMapping("/subscription")
    public RestResponse getSubscriptionVideoPage(PageParams pageParams) {
        PageResult pageResult = videoService.getSubscriptionVideoPage(pageParams);
        return RestResponse.success(pageResult);
    }

    @PostMapping("/upload")
    public RestResponse uploadVideo(@RequestBody Video video) {
        return videoService.uploadVideo(video);
    }

    @GetMapping("/recommend")
    public RestResponse recommend(@RequestParam(required = false, defaultValue = "4") Integer count) {
        return recommendService.recommend(count);
    }

}
