package com.beechannel.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.user.domain.dto.FullUser;
import org.springframework.web.multipart.MultipartFile;

/**
* @author eotouch
* @description 针对表【user】的数据库操作Service
* @createDate 2023-11-14 22:42:59
*/
public interface UserService extends IService<User> {

    /**
     * @description get full user's information
     * @param userId target user's id
     * @param currentUserId current user's id
     * @return com.beechannel.auth.domain.dto.FullUser
     * @author eotouch
     * @date 2023-12-03 16:12
     */
    FullUser getFullInfoById(Long userId, Long currentUserId);

    RestResponse updateAvatar(MultipartFile file);
}
