package com.beechannel.order.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName record
 */
@TableName(value ="record")
@Data
public class Record implements Serializable {
    /**
     * 支付记录表主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 对应订单Id
     */
    @TableField(value = "order_id")
    private Long orderId;

    /**
     * 记录名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 支付记录生成实践
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 外部支付用户名称
     */
    @TableField(value = "other_name")
    private String otherName;

    /**
     * 支付价格
     */
    @TableField(value = "total_price")
    private BigDecimal totalPrice;

    /**
     * 支付状态
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}