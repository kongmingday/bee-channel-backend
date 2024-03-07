package com.beechannel.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.PageResult;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.domain.vo.SearchParams;
import com.beechannel.media.domain.po.Video;

/**
* @author eotouch
* @description 针对表【video】的数据库操作Service
* @createDate 2023-11-27 23:41:32
*/
public interface VideoService extends IService<Video> {

    /**
     * @description get video's information by id
     * @param videoId video's id
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-11-30 21:13
     */
    RestResponse getVideoInfo(Long videoId);

    RestResponse getPersonalVideoList(PageParams pageParams);

    RestResponse searchVideo(SearchParams searchParams);

    RestResponse uploadVideo(Video video);

    PageResult getSubscriptionVideoPage(PageParams pageParams);
}
