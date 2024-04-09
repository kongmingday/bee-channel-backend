package com.beechannel.media.service;

import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.po.PlayList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author eotouch
* @description 针对表【play_list】的数据库操作Service
* @createDate 2024-02-15 12:21:12
*/
public interface PlayListService extends IService<PlayList> {

    RestResponse getPlayListVideoPage(Long playListId, PageParams pageParams);

    RestResponse getWatchLaterVideoPage(PageParams pageParams);

    RestResponse getPlayListByCondition(Integer isWatchLater);
}
