package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.domain.dto.SignPlayVideoList;
import com.beechannel.media.domain.po.PlayVideoList;
import com.beechannel.media.mapper.PlayListMapper;
import com.beechannel.media.mapper.PlayVideoListMapper;
import com.beechannel.media.service.PlayVideoListService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
* @author eotouch
* @description 针对表【play_video_list】的数据库操作Service实现
* @createDate 2023-12-11 15:13:49
*/
@Service
public class PlayVideoListServiceImpl extends ServiceImpl<PlayVideoListMapper, PlayVideoList>
    implements PlayVideoListService{

    @Resource
    private PlayVideoListService playVideoListService;

    @Resource
    private PlayListMapper playListMapper;

    @Resource
    private PlayVideoListMapper playVideoListMapper;


    /**
     * update video play list
     *
     * @param videoId
     * @param playVideoListArray
     */
    @Override
    public void updateVideoInPlaylist(Long videoId, List<PlayVideoList> playVideoListArray) {

        List<Long> originIdList = playVideoListArray.stream().map(PlayVideoList::getPlayListId).collect(Collectors.toList());

        // query the idLIst in db
        LambdaQueryWrapper<PlayVideoList> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.in(PlayVideoList::getPlayListId, originIdList);
        queryWrapper.eq(PlayVideoList::getVideoId, videoId);

        List<PlayVideoList> playVideoLists = playVideoListMapper.selectList(queryWrapper);
        List<Long> collect = playVideoLists.stream().map(PlayVideoList::getPlayListId).collect(Collectors.toList());

        Map<Boolean, List<SignPlayVideoList>> groupMap;

        groupMap = playVideoListArray.stream().map(item -> {
            boolean existedInDB = collect.stream().anyMatch(innerItem -> innerItem.equals(item.getPlayListId()));
            SignPlayVideoList signPlayVideoList = new SignPlayVideoList();
            BeanUtils.copyProperties(item, signPlayVideoList);

            signPlayVideoList.setRequireDelete(existedInDB);
            return signPlayVideoList;
        }).collect(Collectors.groupingBy(SignPlayVideoList::isRequireDelete));


        if(groupMap.containsKey(true)){
            List<Long> requireDelete = groupMap.get(true).stream()
                    .map(SignPlayVideoList::getPlayListId)
                    .collect(Collectors.toList());
            LambdaQueryWrapper<PlayVideoList> deleteWrapper = new LambdaQueryWrapper<>();
            deleteWrapper.in(PlayVideoList::getPlayListId, requireDelete);
            deleteWrapper.eq(PlayVideoList::getVideoId, videoId);

            playVideoListMapper.delete(deleteWrapper);
        }
        if(groupMap.containsKey(false)){
            List<SignPlayVideoList> requireAdd = groupMap.get(false);
            List<PlayVideoList> rebuild = requireAdd.stream().map(item -> {
                item.setVideoId(videoId);
                item.setCreateTime(LocalDateTime.now());
                PlayVideoList playVideoList = new PlayVideoList();
                BeanUtils.copyProperties(item, playVideoList);
                return playVideoList;
            }).collect(Collectors.toList());
            playVideoListService.saveBatch(rebuild);
        }

    }


    /**
     * @description add video to play list
     * @param playListIdList
     * @param videoId
     * @return void
     * @author eotouch
     * @date 2024-02-15 21:53
     */
    @Override
    public void addToPlayList(List<Long> playListIdList, Long videoId) {
        LambdaQueryWrapper<PlayVideoList> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PlayVideoList::getVideoId, videoId);
        queryWrapper.in(PlayVideoList::getPlayListId, playListIdList);
        queryWrapper.select(PlayVideoList::getPlayListId);
        List<PlayVideoList> exist = this.list(queryWrapper);

        List<Long> collect = exist.stream()
                .map(PlayVideoList::getPlayListId)
                .distinct()
                .collect(Collectors.toList());

        List<PlayVideoList> target;
        if(collect.isEmpty()){
            target = playListIdList.stream()
                    .map(item -> {
                        PlayVideoList playVideoList = new PlayVideoList();
                        playVideoList.setCreateTime(LocalDateTime.now());
                        playVideoList.setPlayListId(item);
                        playVideoList.setVideoId(videoId);
                        return playVideoList;
                    }).collect(Collectors.toList());
        }else{
            target = playListIdList.stream()
                    .filter(item -> !collect.contains(item))
                    .map(item -> {
                        PlayVideoList playVideoList = new PlayVideoList();
                        playVideoList.setCreateTime(LocalDateTime.now());
                        playVideoList.setPlayListId(item);
                        playVideoList.setVideoId(videoId);
                        return playVideoList;
                    }).collect(Collectors.toList());
        }

        playVideoListService.saveBatch(target);
    }

    /**
     * get the playlist where the video is in
     *
     * @param videoId
     * @return the playlist where the video is in
     */
    @Override
    public List<PlayVideoList> getVideoInPlaylist(Long videoId) {
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();
        return playVideoListMapper.getPlayVideoListByVideoId(videoId, currentUserId);
    }
}




