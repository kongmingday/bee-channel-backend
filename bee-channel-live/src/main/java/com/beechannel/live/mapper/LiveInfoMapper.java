package com.beechannel.live.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beechannel.live.domain.dto.ActiveLiveInfo;
import com.beechannel.live.domain.po.LiveInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author eotouch
* @description 针对表【live_info】的数据库操作Mapper
* @createDate 2024-01-05 20:48:57
* @Entity com.beechannel.live.domain.po.LiveInfo
*/
public interface LiveInfoMapper extends BaseMapper<LiveInfo> {

    IPage<ActiveLiveInfo> getActiveLivePage(IPage<ActiveLiveInfo> pageInfo);

    ActiveLiveInfo getLiveInformationExtend(@Param("key") String key, @Param("secret") String secret);
}




