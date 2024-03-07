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
 * @TableName like_list
 */
@TableName(value ="like_list")
@Data
public class LikeList implements Serializable {
    /**
     * 点赞表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 点赞来源类型 0-视频 1-评论
     */
    @TableField(value = "derive_type")
    private Integer deriveType;

    /**
     * 点赞来源Id
     */
    @TableField(value = "derive_id")
    private Long deriveId;

    /**
     * 点赞用户
     */
    @TableField(value = "user_from_id")
    private Long userFromId;

    /**
     * 0-点踩  1-点赞
     */
    @TableField(value = "favorite_type")
    private Integer favoriteType;

    /**
     * 点赞时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 被点赞用户 评论必填
     */
    @TableField(value = "user_to_id")
    private Long userToId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}