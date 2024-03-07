package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.PageResult;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.constant.FavoriteType;
import com.beechannel.media.domain.dto.FavoriteActionParam;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.domain.po.LikeList;
import com.beechannel.media.mapper.LikeListMapper;
import com.beechannel.media.mapper.VideoMapper;
import com.beechannel.media.service.LikeListService;
import com.beechannel.media.util.ExtendInfoUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author eotouch
 * @description 针对表【like_list】的数据库操作Service实现
 * @createDate 2023-12-06 17:26:38
 */
@Service
public class LikeListServiceImpl extends ServiceImpl<LikeListMapper, LikeList>
        implements LikeListService {

    @Resource
    private LikeListMapper likeListMapper;

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private ExtendInfoUtil extendInfoUtil;

    @Override
    public RestResponse favoriteChange(FavoriteActionParam param) {
        // check user processing
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();

        Long sourceId = param.getSourceId();
        Integer deriveType = param.getDeriveType();
        Integer favoriteType = param.getFavoriteType();
        Long userToId = param.getUserToId();

        LambdaQueryWrapper<LikeList> likeQuery = new LambdaQueryWrapper<>();
        likeQuery.eq(LikeList::getDeriveId, sourceId);
        likeQuery.eq(LikeList::getDeriveType, deriveType);
        likeQuery.eq(LikeList::getUserFromId, currentUserId);
        likeQuery.eq(userToId != null, LikeList::getUserToId, userToId);
        LikeList like = likeListMapper.selectOne(likeQuery);

        boolean flag;
        if(like == null){
            like = new LikeList();
            like.setDeriveId(sourceId);
            like.setUserFromId(currentUserId);
            like.setDeriveType(deriveType);
            like.setFavoriteType(favoriteType);
            like.setUserToId(userToId);
            like.setCreateTime(LocalDateTime.now());
            flag = likeListMapper.insert(like) > 0;
        }else if(favoriteType.equals(like.getFavoriteType())){
            flag = likeListMapper.deleteById(like.getId()) > 0;
        }else{
            Integer targetFavoriteType = favoriteType.equals(FavoriteType.LIKE.getCode()) ?
                    FavoriteType.LIKE.getCode() : FavoriteType.UNLIKE.getCode();
            like.setFavoriteType(targetFavoriteType);
            flag = likeListMapper.updateById(like) > 0;
        }

        return RestResponse.success(flag);
    }

    @Override
    public RestResponse getLikedVideoPage(PageParams pageParams) {
        Long userId = SecurityUtil.getCurrentUserIdNotNull();
        IPage<SingleVideo> pageInfo = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        videoMapper.getLikedVideoPage(pageInfo, userId);

        PageResult<List<SingleVideo>> pageResult = extendInfoUtil.setAuthorInfo(pageInfo);
        return RestResponse.success(pageResult);
    }
}




