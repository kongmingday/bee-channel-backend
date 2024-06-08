package com.beechannel.live.service;

import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.live.domain.dto.SRSRequestParams;
import com.beechannel.live.domain.po.Live;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author eotouch
* @description 针对表【live】的数据库操作Service
* @createDate 2024-01-05 11:15:36
*/
public interface LiveService extends IService<Live> {

    RestResponse getPersonalLicense();

    int liveInitCheck(SRSRequestParams srsRequestParams);

    RestResponse deleteLicense();

    RestResponse addLicense();

    int stopProcess(SRSRequestParams srsRequestParams);

    RestResponse getActiveLivePage(PageParams pageParams);
}
