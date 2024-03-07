package com.beechannel.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.po.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author eotouch
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-11-14 22:42:59
*/
public interface UserMapper extends BaseMapper<User> {

    FullUser getFullInfoByUserId(@Param("userId") Long userId, @Param("currentUserId") Long currentUserId);

    IPage<FullUser> getFullInfoList(IPage<FullUser> page,
                                          @Param("currentUserId") Long currentUserId,
                                          @Param("keyword") String keyword,
                                          @Param("sortBy") Integer sortBy);

    IPage<User> getSubscriptionPage(IPage<User> pageInfo,
                                    @Param("userFromId") Long userFromId,
                                    @Param("keyword") String keyword);

    List<User> getAllSubscription(Long userId);
}




