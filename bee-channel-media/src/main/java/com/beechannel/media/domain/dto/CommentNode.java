package com.beechannel.media.domain.dto;

import com.beechannel.base.domain.po.User;
import com.beechannel.media.domain.po.Comment;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2023/12/03 23:06
 * @Version 1.0
 */
@Data
public class CommentNode extends Comment implements Serializable {
    private static final long serialVersionUID = 1L;
    private User fromUser;
    private User toUser;
    private Integer childrenCount;
    private Integer likeCount;
    private Integer favoriteType;
}
