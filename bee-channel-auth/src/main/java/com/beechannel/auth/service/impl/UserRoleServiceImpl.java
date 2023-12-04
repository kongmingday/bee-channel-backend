package com.beechannel.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.auth.domain.po.UserRole;
import com.beechannel.auth.service.UserRoleService;
import com.beechannel.auth.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author eotouch
* @description 针对表【user_role】的数据库操作Service实现
* @createDate 2023-11-20 16:13:12
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




