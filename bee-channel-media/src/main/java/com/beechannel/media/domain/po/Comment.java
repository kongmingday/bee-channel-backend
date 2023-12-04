package com.beechannel.media.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
    /**
     * 评论表Id
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 评论媒体来源Id  0-系统消息
     */
    @TableField(value = "derive_id")
    private Long deriveId;

    /**
     * 评论媒体来源类型 0-视频, 1-动态
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 评论用户Id
     */
    @TableField(value = "user_from_id")
    private Long userFromId;

    /**
     * 被评论用户Id
     */
    @TableField(value = "user_to_id")
    private Long userToId;

    /**
     * 评论时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 是否已读 0-未读 1-已读
     */
    @TableField(value = "has_read")
    private Integer hasRead;

    /**
     * 评论内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 父级评论Id
     */
    @TableField(value = "parent_id")
    private Long parentId;

    /**
     * 点赞数量
     */
    @TableField(value = "like_count")
    private Long likeCount;

    /**
     * 点踩数量
     */
    @TableField(value = "unlike_count")
    private Long unlikeCount;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}