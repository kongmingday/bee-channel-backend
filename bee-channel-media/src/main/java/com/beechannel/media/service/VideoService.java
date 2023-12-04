package com.beechannel.media.service;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.po.Video;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
