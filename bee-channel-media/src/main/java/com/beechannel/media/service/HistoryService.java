package com.beechannel.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.media.domain.dto.AddHistory;
import com.beechannel.media.domain.po.History;

/**
* @author eotouch
* @description 针对表【history】的数据库操作Service
* @createDate 2024-02-13 17:50:11
*/
public interface HistoryService extends IService<History> {

    void historyProcess(AddHistory addHistory);

    RestResponse getHistoryPage(PageParams pageParams);
}
