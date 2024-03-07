package com.beechannel.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.constant.CommonConstant;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.user.domain.po.Concern;
import com.beechannel.user.service.ConcernService;
import com.beechannel.user.mapper.ConcernMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
* @author eotouch
* @description 针对表【concern】的数据库操作Service实现
* @createDate 2023-12-06 11:56:03
*/
@Service
public class ConcernServiceImpl extends ServiceImpl<ConcernMapper, Concern>
    implements ConcernService{

    @Resource
    private ConcernMapper concernMapper;

    /**
     * @description the current user's subscribing action
     * @param userToId the id of the user impacted by the action
     * @return com.beechannel.base.domain.vo.RestResponse<java.lang.Boolean>
     * @author eotouch
     * @date 2023-12-08 15:19
     */
    @Override
    public RestResponse<Boolean> subscribeAction(Long userToId) {

        // check user processing
        Long currentUserId = SecurityUtil.getCurrentUserId();
        if(currentUserId == null){
            return RestResponse.validFail(CommonConstant.NO_ONLINE.getMessage());
        }

        if(currentUserId.equals(userToId)){
            return RestResponse.validFail(CommonConstant.SAME_USER.getMessage());
        }

        // get the relationship with current user and target user
        LambdaQueryWrapper<Concern> concernQuery = new LambdaQueryWrapper<>();
        concernQuery.eq(Concern::getUserFromId, currentUserId);
        concernQuery.eq(Concern::getUserToId, userToId);
        boolean exist = concernMapper.selectCount(concernQuery) > 0;

        // if exist, unconcern
        // else concern
        boolean flag;
        if(exist){
            flag = concernMapper.delete(concernQuery) > 0;
        }else {
            Concern concern = new Concern();
            concern.setUserFromId(currentUserId);
            concern.setUserToId(userToId);
            concern.setCreateTime(LocalDateTime.now());
            flag = concernMapper.insert(concern) > 0;
        }
        return RestResponse.success(flag);
    }
}




