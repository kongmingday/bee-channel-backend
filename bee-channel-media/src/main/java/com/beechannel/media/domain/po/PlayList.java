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
 * @TableName play_list
 */
@TableName(value ="play_list")
@Data
public class PlayList implements Serializable {
    /**
     * 播放列表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 播放列表名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 列表创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建用户Id
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * 列表状态 0-隐藏 1-公开
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}