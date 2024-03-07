package com.beechannel.user.service;

import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.user.domain.po.Concern;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author eotouch
* @description 针对表【concern】的数据库操作Service
* @createDate 2023-12-06 11:56:03
*/
public interface ConcernService extends IService<Concern> {

    RestResponse<Boolean> subscribeAction(Long userToId);
}
