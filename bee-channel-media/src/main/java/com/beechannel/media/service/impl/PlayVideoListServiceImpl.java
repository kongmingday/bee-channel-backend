package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.media.domain.po.PlayVideoList;
import com.beechannel.media.mapper.PlayListMapper;
import com.beechannel.media.mapper.PlayVideoListMapper;
import com.beechannel.media.service.PlayVideoListService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
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
                        playVideoList.setCreatTime(LocalDateTime.now());
                        playVideoList.setPlayListId(item);
                        playVideoList.setVideoId(videoId);
                        return playVideoList;
                    }).collect(Collectors.toList());
        }else{
            target = playListIdList.stream()
                    .filter(item -> !collect.contains(item))
                    .map(item -> {
                        PlayVideoList playVideoList = new PlayVideoList();
                        playVideoList.setCreatTime(LocalDateTime.now());
                        playVideoList.setPlayListId(item);
                        playVideoList.setVideoId(videoId);
                        return playVideoList;
                    }).collect(Collectors.toList());
        }

        playVideoListService.saveBatch(target);
    }
}




