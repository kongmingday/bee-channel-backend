package com.beechannel.order.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 
 * @TableName trade
 */
@TableName(value ="trade")
@Data
public class Trade implements Serializable {
    /**
     * 订单表主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 订单价格
     */
    @TableField(value = "total_price")
    private BigDecimal totalPrice;

    /**
     * 订单生成日期
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 订单用户Id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 订单详情Json
     */
    @TableField(value = "details")
    private String details;

    /**
     * 订单描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 订单状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 订单来源Id
     */
    @TableField(value = "derive_id")
    private String deriveId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}