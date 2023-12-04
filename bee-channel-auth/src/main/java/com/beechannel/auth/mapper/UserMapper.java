package com.beechannel.auth.mapper;

import com.beechannel.auth.domain.dto.FullUser;
import com.beechannel.auth.domain.dto.UserExt;
import com.beechannel.base.domain.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author eotouch
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-11-14 22:42:59
*/
public interface UserMapper extends BaseMapper<User> {

    UserExt getUserAndRoleList(String keyword);

    FullUser getFullInfoByUserId(Long userId);

}




