package com.beechannel.live.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName supervise_live
 */
@TableName(value ="supervise_live")
@Data
public class SuperviseLive implements Serializable {
    /**
     * 直播审核主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 被审核用户Id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 审核原因
     */
    @TableField(value = "reason")
    private String reason;

    /**
     * 审核用户Id
     */
    @TableField(value = "supervisor_id")
    private Long supervisorId;

    /**
     * 建立时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 审核时间
     */
    @TableField(value = "supervise_time")
    private LocalDateTime superviseTime;

    /**
     * 审核状态
     */
    @TableField(value = "status")
    private LocalDateTime status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}