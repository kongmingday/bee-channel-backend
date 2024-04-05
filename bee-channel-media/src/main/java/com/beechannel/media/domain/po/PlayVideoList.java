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
 * @TableName play_video_list
 */
@TableName(value ="play_video_list")
@Data
public class PlayVideoList implements Serializable {
    /**
     * 播放列表中间表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 播放列表Id
     */
    @TableField(value = "play_list_id")
    private Long playListId;

    /**
     * 视频Id
     */
    @TableField(value = "video_id")
    private Long videoId;

    /**
     * 视频加入时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}