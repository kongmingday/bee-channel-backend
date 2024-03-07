package com.beechannel.media.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.beechannel.base.constant.InnerRpcStatus;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.PageParams;
import com.beechannel.base.domain.vo.PageResult;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.util.SecurityUtil;
import com.beechannel.media.domain.dto.AddHistory;
import com.beechannel.media.domain.dto.HistoryVideo;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.domain.po.History;
import com.beechannel.media.feign.UserClient;
import com.beechannel.media.mapper.HistoryMapper;
import com.beechannel.media.mapper.VideoMapper;
import com.beechannel.media.service.HistoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author eotouch
* @description 针对表【history】的数据库操作Service实现
* @createDate 2024-02-13 17:50:11
*/
@Service
public class HistoryServiceImpl extends ServiceImpl<HistoryMapper, History>
    implements HistoryService{

    @Resource
    private UserClient userClient;

    @Resource
    private HistoryMapper historyMapper;

    @Resource
    private VideoMapper videoMapper;

    /**
     * @description the browse action process
     * @param addHistory history information
     * @return void
     * @author eotouch
     * @date 2024-02-15 15:25
     */
    @Transactional
    @Override
    public void historyProcess(AddHistory addHistory) {

        Long userId = SecurityUtil.getCurrentUserIdNotNull();
        Long videoId = addHistory.getVideoId();
        Long duration = addHistory.getDuration();
        LambdaQueryWrapper<History> historyQueryWrapper = new LambdaQueryWrapper<>();
        historyQueryWrapper.eq(History::getUserId, userId);
        historyQueryWrapper.eq(History::getVideoId, videoId);
        History history = historyMapper.selectOne(historyQueryWrapper);

        if(history == null){
            history = new History();
            BeanUtils.copyProperties(addHistory, history);
            LocalDateTime now = LocalDateTime.now();
            history.setCreateTime(now);
            history.setUpdateTime(now);
            history.setUserId(userId);
            historyMapper.insert(history);
            videoMapper.historyProcess(videoId, duration, true);
        }else{
            history.setUpdateTime(LocalDateTime.now());
            history.setDuration(history.getDuration() + duration);
            history.setPausePoint(addHistory.getPausePoint());
            historyMapper.updateById(history);
            videoMapper.historyProcess(videoId, duration, false);
        }
    }

    /**
     * @description get the history by the user's id
     * @param pageParams the page params
     * @return com.beechannel.base.domain.vo.RestResponse
     * @author eotouch
     * @date 2024-02-15 15:26
     */
    @Override
    public RestResponse getHistoryPage(PageParams pageParams) {

        Long userId = SecurityUtil.getCurrentUserIdNotNull();
        IPage<HistoryVideo> pageInfo = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());

        videoMapper.getHistoryVideoPage(pageInfo, userId);
        List<HistoryVideo> records = pageInfo.getRecords();
        List<Long> authorIdList = records.stream().map(item -> item.getVideo().getAuthorId()).collect(Collectors.toList());
        RestResponse<List<User>> rpcResult = userClient.getUserInfoByIdList(authorIdList);

        if(InnerRpcStatus.ERROR.getCode().equals(rpcResult.getCode())){
            return rpcResult;
        }

        List<User> authorList = rpcResult.getResult();
        records.forEach(item -> {
            SingleVideo video = item.getVideo();
            Long authorId = video.getAuthorId();

            User user = authorList.stream().filter(element -> authorId.equals(element.getId())).findFirst().get();
            FullUser author = new FullUser();
            BeanUtils.copyProperties(user, author);
            video.setAuthor(author);
            item.setVideo(video);
        });

        PageResult pageResult = new PageResult(records, (int) pageInfo.getTotal());
        return RestResponse.success(pageResult);
    }
}




