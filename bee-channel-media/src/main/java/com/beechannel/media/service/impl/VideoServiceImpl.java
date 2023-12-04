package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.constant.CommentDeriveType;
import com.beechannel.media.domain.dto.FullUser;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.domain.po.Comment;
import com.beechannel.media.domain.po.Video;
import com.beechannel.media.feign.UserClient;
import com.beechannel.media.mapper.CommentMapper;
import com.beechannel.media.service.VideoService;
import com.beechannel.media.mapper.VideoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author eotouch
* @description 针对表【video】的数据库操作Service实现
* @createDate 2023-11-27 23:41:32
*/
@Service
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

    @Resource
    private UserClient userClient;

    @Resource
    private VideoMapper videoMapper;

    @Resource
    private CommentMapper commentMapper;

    /**
     * @description get video's information by id
     * @param videoId video's id
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-11-30 21:13
     */
    @Override
    public RestResponse getVideoInfo(Long videoId) {

        // get the relationship by current user
        User currentUser = SecurityUtil.getCurrentUser();
        Long currentId = null;
        if (currentUser != null){
            currentId = currentUser.getId();
        }

        // get the video and the user
        Video video = videoMapper.selectById(videoId);
        RestResponse<FullUser> userInfo = userClient.getFullUserInfo(videoId, currentId);
        FullUser author = userInfo.getResult();

        // get the comment's count of the video
        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<>();
        commentQuery.eq(Comment::getType, CommentDeriveType.VIDEO.getCode());
        commentQuery.eq(Comment::getDeriveId, videoId);
        Long commentCount = commentMapper.selectCount(commentQuery);

        // encapsulate the result
        SingleVideo singleVideo = new SingleVideo();
        BeanUtils.copyProperties(video,singleVideo);
        singleVideo.setAuthor(author);
        singleVideo.setCommentCount(commentCount);

        return RestResponse.success(singleVideo);
    }
}




