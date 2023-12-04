package com.beechannel.auth.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 
 * @TableName concern
 */
@TableName(value ="concern")
@Data
public class Concern implements Serializable {
    /**
     * 关注表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关注用户Id
     */
    @TableField(value = "user_from_id")
    private Long userFromId;

    /**
     * 被关注用户Id
     */
    @TableField(value = "user_to_id")
    private Long userToId;

    /**
     * 关注时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}