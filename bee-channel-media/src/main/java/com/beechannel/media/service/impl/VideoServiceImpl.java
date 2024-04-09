package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.constant.AuditStatus;
import com.beechannel.base.constant.InnerRpcStatus;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.PageResult;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.domain.vo.SearchParams;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.constant.DeriveType;
import com.beechannel.media.constant.SearchSortType;
import com.beechannel.media.domain.dto.AuditVideoItem;
import com.beechannel.media.domain.dto.SimpleVideo;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.domain.po.Comment;
import com.beechannel.media.domain.po.Supervise;
import com.beechannel.media.domain.po.Video;
import com.beechannel.media.feign.UserClient;
import com.beechannel.media.mapper.CommentMapper;
import com.beechannel.media.mapper.SuperviseMapper;
import com.beechannel.media.mapper.VideoMapper;
import com.beechannel.media.service.VideoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
     * @description search video by params
     * @param searchParams search params
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2024-01-10 16:08
     */
    @Override
    public RestResponse searchVideo(SearchParams searchParams) {
        // search video list
        LambdaQueryWrapper<Video> videoQuery = new LambdaQueryWrapper<>();
        String keyword = searchParams.getKeyword();
        Integer categoryId = searchParams.getCategoryId();
        Integer sortBy = searchParams.getSortBy();
        videoQuery.like(
                StringUtils.hasText(keyword),
                Video::getTitle,
                keyword
        );
        videoQuery.eq(
                categoryId != null,
                Video::getCategoryId,
                categoryId
        );
        videoQuery.eq(Video::getStatus, AuditStatus.APPROVE.getId());
        videoQuery.orderBy(SearchSortType.NEWEST.getId() == sortBy, false, Video::getPublicTime);
        videoQuery.orderBy(SearchSortType.MOST.getId() == sortBy, false, Video::getSawTime);
        videoQuery.orderBy(true, false, Video::getId);
        IPage<Video> pageInfo = new Page<>(searchParams.getPageNo(), searchParams.getPageSize());


        videoMapper.selectPage(pageInfo, videoQuery);
        List<Video> records = pageInfo.getRecords();
        int total = (int) pageInfo.getTotal();
        if(total <= 0){
            PageResult<List<SingleVideo>> pageResult = new PageResult<>(Collections.emptyList(), 0);
            return RestResponse.success(pageResult);
        }

        // get all user from videoId
        List<Long> userIdList = records.stream().map(Video::getAuthorId).distinct().collect(Collectors.toList());
        RestResponse<List<User>> response = userClient.getUserInfoByIdList(userIdList);
        if(response.getCode() == InnerRpcStatus.ERROR.getCode()){
            BeeChannelException.cast("search video has error");
        }

        Map<Long, List<User>> userMap = response.getResult().stream().collect(Collectors.groupingBy(User::getId));

        List<SingleVideo> collect = records.stream().map(item -> {
            SingleVideo singleVideo = new SingleVideo();
            BeanUtils.copyProperties(item, singleVideo);

            User sourceUser = userMap.get(item.getAuthorId()).get(0);
            FullUser fullUser = new FullUser();
            BeanUtils.copyProperties(sourceUser, fullUser);
            singleVideo.setAuthor(fullUser);
            return singleVideo;
        }).collect(Collectors.toList());

        PageResult<List<SingleVideo>> pageResult = new PageResult<>();
        pageResult.setData(collect);
        pageResult.setTotal(total);
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

    /**
     * @description get the subscription video of the user's subscribed
     * @param pageParams the page params
     * @return com.beechannel.base.domain.vo.PageResult
     * @author eotouch
     * @date 2024-02-12 18:26
     */
    @Override
    public PageResult getSubscriptionVideoPage(PageParams pageParams) {
        RestResponse<List<User>> allSubscription = userClient.getAllSubscription();
        List<User> authorList = allSubscription.getResult();
        if(allSubscription.getCode() == InnerRpcStatus.ERROR.getCode() || authorList.isEmpty()){
           return new PageResult(Collections.emptyList(), 0);
        }
        List<Long> idList = authorList.stream().map(User::getId).collect(Collectors.toList());
        IPage<Video> pageInfo = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        videoMapper.getSubscriptionVideoPage(pageInfo, idList);

        List<SimpleVideo> collect = pageInfo.getRecords().stream().map(item -> {
            SimpleVideo simpleVideo = new SimpleVideo();
            BeanUtils.copyProperties(item, simpleVideo);
            User author = authorList.stream()
                    .filter(element -> element.getId().equals(item.getAuthorId()))
                    .findFirst().get();
            simpleVideo.setAuthor(author);
            return simpleVideo;
        }).collect(Collectors.toList());

        return new PageResult<>(collect, (int)pageInfo.getTotal());
    }
}




