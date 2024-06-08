package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.PageResult;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.domain.po.PlayList;
import com.beechannel.media.domain.po.PlayVideoList;
import com.beechannel.media.constant.PlayListType;
import com.beechannel.media.mapper.PlayListMapper;
import com.beechannel.media.mapper.PlayVideoListMapper;
import com.beechannel.media.mapper.VideoMapper;
import com.beechannel.media.service.PlayListService;
import com.beechannel.media.util.ExtendInfoUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
* @author eotouch
* @description 针对表【play_list】的数据库操作Service实现
* @createDate 2024-02-15 12:21:12
*/
@Service
public class PlayListServiceImpl extends ServiceImpl<PlayListMapper, PlayList>
    implements PlayListService{

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private ExtendInfoUtil extendInfoUtil;

    @Resource
    private PlayListMapper playListMapper;

    @Resource
    private PlayVideoListMapper playVideoListMapper;

    @Override
    public RestResponse getWatchLaterVideoPage(PageParams pageParams) {

        Long userId = SecurityUtil.getCurrentUserIdNotNull();
        IPage<SingleVideo> pageInfo = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        videoMapper.getWatchLaterVideoPage(pageInfo, userId);

        PageResult<List<SingleVideo>> pageResult = extendInfoUtil.setAuthorInfo(pageInfo);
        return RestResponse.success(pageResult);
    }

    @Override
    public RestResponse getPlayListByCondition(Integer isWatchLater) {
        Long userId = SecurityUtil.getCurrentUserIdNotNull();

        boolean watchLaterSignal = isWatchLater != null && isWatchLater == 1;

        LambdaQueryWrapper<PlayList> playListQueryWrapper = new LambdaQueryWrapper<>();
        playListQueryWrapper.eq(PlayList::getCreateUser, userId);
        if(watchLaterSignal){
            playListQueryWrapper.eq(PlayList::getName, PlayListType.WATCH_LATER.getName());
        }
        List<PlayList> list = playListMapper.selectList(playListQueryWrapper);

        if(!watchLaterSignal || !list.isEmpty()){
            return RestResponse.success(list);
        }

        PlayList playList = new PlayList();
        playList.setName(PlayListType.WATCH_LATER.getName());
        playList.setCreateTime(LocalDateTime.now());
        playList.setCreateUser(userId);
        playList.setStatus(1);
        playListMapper.insert(playList);

        list = Arrays.asList(playList);
        return RestResponse.success(list);
    }

    @Override
    public RestResponse deletePlayListById(Long playListId) {
        Long userId = SecurityUtil.getCurrentUserIdNotNull();

        LambdaQueryWrapper<PlayList> playListLambdaQueryWrapper = new LambdaQueryWrapper<>();
        playListLambdaQueryWrapper.eq(PlayList::getId, playListId);
        playListLambdaQueryWrapper.eq(PlayList::getCreateUser, userId);
        boolean isDeleted = playListMapper.delete(playListLambdaQueryWrapper) > 0;

        if(!isDeleted){
           return RestResponse.success();
        }

        LambdaQueryWrapper<PlayVideoList> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlayVideoList::getPlayListId, playListId);
        playVideoListMapper.delete(queryWrapper);
        return RestResponse.success();
    }

    @Override
    public RestResponse getPlayListVideoPage(Long playListId, PageParams pageParams) {

        IPage<SingleVideo> pageInfo = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        videoMapper.getPlayListVideoPage(pageInfo, playListId);

        PageResult<List<SingleVideo>> pageResult = extendInfoUtil.setAuthorInfo(pageInfo);
        return RestResponse.success(pageResult);
    }
}




