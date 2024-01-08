package com.beechannel.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.po.User;

/**
* @author eotouch
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-11-14 22:42:59
*/
public interface UserMapper extends BaseMapper<User> {

    FullUser getFullInfoByUserId(Long userId);

}




