package com.beechannel.media.service;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.po.PlayVideoList;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author eotouch
* @description 针对表【play_video_list】的数据库操作Service
* @createDate 2023-12-11 15:13:49
*/
public interface PlayVideoListService extends IService<PlayVideoList> {

    void addToPlayList(List<Long> playListIdList, Long videoId);

    List<PlayVideoList> getVideoInPlaylist(Long videoId);

    void updateVideoInPlaylist(Long videoId, List<PlayVideoList> playVideoList);

    RestResponse removeByRelation(Long playListId, Long videoId);
}
