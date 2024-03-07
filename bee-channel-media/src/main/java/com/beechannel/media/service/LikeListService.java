package com.beechannel.media.service;

import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.dto.FavoriteActionParam;
import com.beechannel.media.domain.po.LikeList;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author eotouch
* @description 针对表【like_list】的数据库操作Service
* @createDate 2023-12-06 17:26:38
*/
public interface LikeListService extends IService<LikeList> {

    RestResponse favoriteChange(FavoriteActionParam param);

    RestResponse getLikedVideoPage(PageParams pageParams);
}
