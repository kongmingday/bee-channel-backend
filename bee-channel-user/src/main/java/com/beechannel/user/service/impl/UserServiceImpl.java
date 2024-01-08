package com.beechannel.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.constant.CommonConstant;
import com.beechannel.base.constant.InnerRpcStatus;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.user.domain.dto.FileUploadResult;
import com.beechannel.user.domain.po.Concern;
import com.beechannel.user.feign.FileUploadClient;
import com.beechannel.user.mapper.ConcernMapper;
import com.beechannel.user.mapper.UserMapper;
import com.beechannel.user.service.UserService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
* @author eotouch
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-11-14 22:42:59
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ConcernMapper concernMapper;

    @Resource
    private FileUploadClient fileUploadClient;

    /**
     * @description get full user's information
     * @param userId target user's id
     * @param currentUserId current user's id
     * @return com.beechannel.auth.domain.dto.FullUser
     * @author eotouch
     * @date 2023-12-03 16:12
     */
    @Override
    public FullUser getFullInfoById(Long userId, Long currentUserId) {
        FullUser fullUser = userMapper.getFullInfoByUserId(userId);
        if(currentUserId != null){
            boolean relationship = getRelationship(currentUserId, userId);
            fullUser.setHasConcern(relationship);
        }
        return fullUser;
    }

    /**
     * @description upload user's avatar
     * @param file
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-12-20 23:34
     */
    @GlobalTransactional
    @Override
    public RestResponse updateAvatar(MultipartFile file) {

        Long currentUserId = SecurityUtil.getCurrentUserId();
        if (currentUserId == null) {
            BeeChannelException.cast(CommonConstant.NO_ONLINE.getMessage());
        }

        RestResponse<FileUploadResult> restResponse = fileUploadClient.singleUploadFile(file);
        int rpcStatus = restResponse.getCode();

        if(rpcStatus == InnerRpcStatus.ERROR.getCode()){
            return RestResponse.validFail("upload avatar has error");
        }
        String uploadAddress = restResponse.getResult().getFilePath();

        User user = new User();
        user.setId(currentUserId);
        user.setProfile(uploadAddress);
        boolean uploadFlag = userMapper.updateById(user) > 0;
        return uploadFlag ? RestResponse.success(uploadAddress, null) : RestResponse.validFail("upload avatar fail");
    }


    /**
     * @description get relationship from userFrom to userTO
     *              if true, has concerned
     *              or false, hasn't concerned
     * @param userFromId
     * @param userToId
     * @return boolean
     * @author eotouch
     * @date 2023-12-03 16:34
     */
    public boolean getRelationship(Long userFromId, Long userToId){

        LambdaQueryWrapper<Concern> concernQuery = new LambdaQueryWrapper<>();
        concernQuery.eq(Concern::getUserFromId, userFromId);
        concernQuery.eq(Concern::getUserToId, userToId);
        Long count = concernMapper.selectCount(concernQuery);
        return count > 0;
    }
}




