package com.beechannel.live.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName live
 */
@TableName(value ="live")
@Data
public class Live implements Serializable {
    /**
     * 推流表主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 推流用户Id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 推流键名
     */
    @TableField(value = "live_key")
    private String liveKey;

    /**
     * 推流密钥
     */
    @TableField(value = "live_secret")
    private String liveSecret;

    /**
     * 直播间状态 0-封禁 1-启用
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 推流状态 0-未推流 1-推流中
     */
    @TableField(value = "cureent_status")
    private Integer cureentStatus;

    /**
     * 信用分值
     */
    @TableField(value = "credit_score")
    private Integer creditScore;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}