package com.beechannel.media.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName history
 */
@TableName(value ="history")
@Data
public class History implements Serializable {
    /**
     * 历史记录表Id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 浏览视频Id
     */
    @TableField(value = "video_id")
    private Long videoId;

    /**
     * 浏览用户Id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 浏览时长毫秒值
     */
    @TableField(value = "duration")
    private Long duration;

    /**
     * 初次浏览时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 最后浏览时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    /**
     * 暂停时间
     */
    @TableField(value = "pause_point")
    private String pausePoint;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}