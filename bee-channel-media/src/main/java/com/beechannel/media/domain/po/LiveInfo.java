package com.beechannel.media.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName live_info
 */
@TableName(value ="live_info")
@Data
public class LiveInfo implements Serializable {
    /**
     * 
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 
     */
    @TableField(value = "live_id")
    private Long liveId;

    /**
     * 
     */
    @TableField(value = "cover_path")
    private String coverPath;

    /**
     * 
     */
    @TableField(value = "cover_id")
    private Long coverId;

    /**
     * 
     */
    @TableField(value = "title")
    private String title;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}