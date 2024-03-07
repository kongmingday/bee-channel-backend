package com.beechannel.media.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.beechannel.base.constant.InnerRpcStatus;
import com.beechannel.base.domain.dto.FullUser;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.PageResult;
import com.beechannel.base.domain.vo.RestResponse;
import com.beechannel.base.exception.BeeChannelException;
import com.beechannel.media.domain.dto.SingleVideo;
import com.beechannel.media.feign.UserClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description the extend information util
 * @Author eotouch
 * @Date 2024/02/15 15:33
 * @Version 1.0
 */
@Component
public class ExtendInfoUtil {

    @Resource
    private UserClient userClient;

    public PageResult<List<SingleVideo>> setAuthorInfo(IPage<SingleVideo> pageInfo){
        List<SingleVideo> records = pageInfo.getRecords();

        if(records.isEmpty()){
            return new PageResult<>();
        }

        List<Long> authorIdList = records.stream().map(SingleVideo::getAuthorId).collect(Collectors.toList());
        RestResponse<List<User>> rpcResult = userClient.getUserInfoByIdList(authorIdList);

        if(InnerRpcStatus.ERROR.getCode().equals(rpcResult.getCode())){
            BeeChannelException.cast(InnerRpcStatus.ERROR.getDescription());
        }

        List<User> authorList = rpcResult.getResult();
        records.forEach(item -> {
            Long authorId = item.getAuthorId();

            User user = authorList.stream().filter(element -> authorId.equals(element.getId())).findFirst().get();
            FullUser author = new FullUser();
            BeanUtils.copyProperties(user, author);
            item.setAuthor(author);
        });
        return new PageResult<>(records, (int) pageInfo.getTotal());
    }

}
