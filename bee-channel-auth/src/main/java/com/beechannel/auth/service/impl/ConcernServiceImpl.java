package com.beechannel.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.auth.domain.po.Concern;
import com.beechannel.auth.service.ConcernService;
import com.beechannel.auth.mapper.ConcernMapper;
import org.springframework.stereotype.Service;

/**
* @author eotouch
* @description 针对表【concern】的数据库操作Service实现
* @createDate 2023-12-03 13:54:32
*/
@Service
public class ConcernServiceImpl extends ServiceImpl<ConcernMapper, Concern>
    implements ConcernService{

}




