package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.PageResult;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.constant.DeriveType;
import com.beechannel.media.domain.dto.AuditVideoItem;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.domain.po.Comment;
import com.beechannel.media.domain.po.Supervise;
import com.beechannel.media.domain.po.Video;
import com.beechannel.media.feign.UserClient;
import com.beechannel.media.mapper.CommentMapper;
import com.beechannel.media.mapper.SuperviseMapper;
import com.beechannel.media.mapper.VideoMapper;
import com.beechannel.media.service.VideoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

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

    @Resource
    private SuperviseMapper superviseMapper;

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
        Long currentId = SecurityUtil.getCurrentUserId();

        // get the video and the user
        SingleVideo video = videoMapper.getFullVideoInfo(videoId, currentId);
        Long authorId = video.getAuthorId();
        RestResponse<FullUser> userInfo = userClient.getFullUserInfo(authorId, currentId);
        FullUser author = userInfo.getResult();

        // get the comment's count of the video
        LambdaQueryWrapper<Comment> commentQuery = new LambdaQueryWrapper<>();
        commentQuery.eq(Comment::getDeriveType, DeriveType.VIDEO.getCode());
        commentQuery.eq(Comment::getDeriveId, videoId);
        Long commentCount = commentMapper.selectCount(commentQuery);

        // encapsulate the result
        video.setAuthor(author);
        video.setCommentCount(commentCount);

        return RestResponse.success(video);
    }

    /**
     * @description get the personal video list by current user id
     * @param pageParams page params
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-12-29 21:35
     */
    @Override
    public RestResponse getPersonalVideoList(PageParams pageParams) {

        // check user
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();
        IPage<AuditVideoItem> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        videoMapper.getPersonalVideoList(page, currentUserId);

        PageResult pageResult = new PageResult(page.getRecords(), (int) page.getTotal());
        return RestResponse.success(pageResult);
    }

    /**
     * @description upload video information
     * @param video the video information
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2024-01-03 21:42
     */
    @Override
    @Transactional
    public RestResponse uploadVideo(Video video) {
        Long currentUserId= SecurityUtil.getCurrentUserIdNotNull();
        video.setAuthorId(currentUserId);
        video.setStatus(0);
        video.setUpTime(LocalDateTime.now());
        boolean videoInsertFlag = videoMapper.insert(video) > 0;
        if(!videoInsertFlag){
            BeeChannelException.cast("the video information upload has error");
        }

        Supervise supervise = new Supervise();
        supervise.setVideoId(video.getId());
        supervise.setStatus(0);
        supervise.setCreateTime(LocalDateTime.now());
        boolean superviseInsertFlag = superviseMapper.insert(supervise) > 0;
        if(!superviseInsertFlag){
            BeeChannelException.cast("the video information upload has error");
        }
        return RestResponse.success();
    }
}




