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
 * @TableName supervise
 */
@TableName(value ="supervise")
@Data
public class Supervise implements Serializable {
    /**
     * 审核表主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 审核视频Id
     */
    @TableField(value = "video_id")
    private Long videoId;

    /**
     * 审核员Id
     */
    @TableField(value = "supervisor_id")
    private Long supervisorId;

    /**
     * 审核状态 0 - 尚未审核 1 - 审核不通过 2 - 审核通过
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 审核原因
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 审核时间
     */
    @TableField(value = "supervise_time")
    private LocalDateTime superviseTime;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}