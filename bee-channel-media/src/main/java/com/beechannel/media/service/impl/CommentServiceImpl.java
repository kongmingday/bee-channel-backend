package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.constant.CommonConstant;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.PageResult;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.domain.dto.CommentNode;
import com.beechannel.media.domain.dto.CommitCommentParam;
import com.beechannel.media.domain.po.Comment;
import com.beechannel.media.domain.po.LikeList;
import com.beechannel.media.feign.UserClient;
import com.beechannel.media.mapper.LikeListMapper;
import com.beechannel.media.service.CommentService;
import com.beechannel.media.mapper.CommentMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
* @author eotouch
* @description 针对表【comment】的数据库操作Service实现
* @createDate 2023-12-03 22:06:07
*/
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment>
    implements CommentService{

    @Resource
    private UserClient userClient;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private LikeListMapper likeMapper;

    /**
     * @description get comment page by videoId
     * @param videoId target video id
     * @param orderBy sort
     * @param pageParams page params
     * @return RestResponse
     * @author eotouch
     * @date 2023-12-03 23:13
     */
    @Override
    public RestResponse getCommentPageByVideoId(Long videoId, Integer orderBy, PageParams pageParams) {

        // get currentUserId
        Long currentUserId = SecurityUtil.getCurrentUserId();

        // get root comment
        Page<CommentNode> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        if(orderBy == null){
            orderBy = 0;
        }
        commentMapper.getCommentPageByVideoId(page, videoId, orderBy, currentUserId);

        // process
        List<CommentNode> records = page.getRecords();
        if(!records.isEmpty()){
            // get the id of all user
            List<Long> userFromIdList = records.stream()
                    .map(CommentNode::getUserFromId).distinct().collect(Collectors.toList());
            // get all user's message
            List<User> userList = userClient.getUserInfoByIdList(userFromIdList).getResult();

            // find users from rpc result and set in records
            records = records.stream().map(item -> {
                Long userFromId = item.getUserFromId();
                User fromUser = filterAndGet(userFromId, userList);
                item.setFromUser(fromUser);
                return item;
            }).collect(Collectors.toList());
        }

        PageResult pageResult = new PageResult(records, (int) page.getTotal());
        return RestResponse.success(pageResult);
    }

    /**
     * @description get children comment by the parent's id
     * @param parentId the parent's id of the comment
     * @param pageParams page params
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-12-05 15:48
     */
    @Override
    public RestResponse getChildrenCommentByParentId(Long parentId, PageParams pageParams) {

        // get currentUserId
        Long currentUserId = SecurityUtil.getCurrentUserId();

        // get children comment
        IPage<CommentNode> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        commentMapper.getChildrenCommentByParentId(page, parentId, currentUserId);

        // process
        List<CommentNode> records = page.getRecords();
        List<Long> userFromIdList = records.stream().map(Comment::getUserFromId).collect(Collectors.toList());
        List<Long> userToIdList = records.stream().map(Comment::getUserToId).collect(Collectors.toList());

        // distinct duplicated number
        List<Long> collect = Stream.concat(userFromIdList.stream(), userToIdList.stream())
                .distinct().collect(Collectors.toList());

        // get all user's message
        List<User> userList = userClient.getUserInfoByIdList(collect).getResult();

        records = records.stream().map(item -> {
            CommentNode node = new CommentNode();
            BeanUtils.copyProperties(item, node);
            return node;
        }).map(node -> {
            Long userFromId = node.getUserFromId();
            User fromUser = filterAndGet(userFromId, userList);
            node.setFromUser(fromUser);

            Long userToId = node.getUserToId();
            User toUser = filterAndGet(userToId, userList);
            node.setToUser(toUser);
            return node;
        }).collect(Collectors.toList());


        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("data", records);
        resultMap.put("total", page.getTotal());
        return RestResponse.success(resultMap);
    }


    /**
     * @description delete comment by its id
     * @param commentId the comment's id
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-12-09 21:01
     */
    @Override
    @Transactional
    public RestResponse deleteComment(Long commentId) {

        // current user check
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();

        // not null
        Comment comment = commentMapper.selectById(commentId);
        if(comment == null){
            return RestResponse.success(true);
        }

        // verify user
        Long userFromId = comment.getUserFromId();
        if(!userFromId.equals(currentUserId)){
            BeeChannelException.cast(CommonConstant.NO_AUTHENTICATION.getMessage());
        }

        // delete the comment data based on whether it is a parent node (parentId equals 0L)
        Long parentId = comment.getParentId();
        if(parentId.equals(0L)){
            // remove like and comment, like must be first
            likeMapper.deleteByParentCommentId(commentId);
            commentMapper.deleteByParentId(commentId);
        }else{
            LambdaQueryWrapper<LikeList> likeQuery = new LambdaQueryWrapper<>();
            likeQuery.eq(LikeList::getDeriveType, 2);
            likeQuery.eq(LikeList::getDeriveId, commentId);
            likeMapper.delete(likeQuery);

            commentMapper.deleteById(commentId);
        }

        return RestResponse.success(true);
    }


    /**
     * @description commit comment
     * @param param
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2023-12-10 18:10
     */
    @Override
    public RestResponse commitComment(CommitCommentParam param) {
        // check user
        Long currentUserId = SecurityUtil.getCurrentUserIdNotNull();

        Comment comment = new Comment();
        comment.setUserFromId(currentUserId);
        BeanUtils.copyProperties(param, comment);
        comment.setCreateTime(LocalDateTime.now());
        comment.setHasRead(0);
        boolean flag = commentMapper.insert(comment) > 0;
        return RestResponse.success(flag);
    }


    /**
     * @description get the user from userList by id
     * @param id target id
     * @param userList source userList
     * @return com.beechannel.base.domain.po.User
     * @author eotouch
     * @date 2023-12-06 17:14
     */
    private User filterAndGet(Long id, List<User> userList){
        if(!id.equals(0L)){
            Optional<User> result = userList.stream()
                    .filter(target -> target.getId().equals(id))
                    .findFirst();

            return result.get();
        }
        return null;
    }
}




