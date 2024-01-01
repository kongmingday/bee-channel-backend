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
 * @TableName video
 */
@TableName(value ="video")
@Data
public class Video implements Serializable {
    /**
     * 视频表主键
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 视频作者Id
     */
    @TableField(value = "author_id")
    private Long authorId;

    /**
     * 视频标题
     */
    @TableField(value = "title")
    private String title;

    /**
     * 视频介绍
     */
    @TableField(value = "introduction")
    private String introduction;

    /**
     * 视频标签 ([] - 无, ["nice job"])
     */
    @TableField(value = "tag")
    private String tag;

    /**
     * 审核状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 视频分类Id
     */
    @TableField(value = "category_id")
    private Integer categoryId;

    /**
     * 封面保存路径
     */
    @TableField(value = "cover_path")
    private String coverPath;

    /**
     * 视频保存路径
     */
    @TableField(value = "save_path")
    private String savePath;


    /**
     * 视频上传时间
     */
    @TableField(value = "up_time")
    private LocalDateTime upTime;

    /**
     * 视频公布时间 null-过审及公布
     */
    @TableField(value = "public_time")
    private LocalDateTime publicTime;

    /**
     * 被浏览时间
     */
    @TableField(value = "saw_time")
    private Long sawTime;

    /**
     * 被浏览数
     */
    @TableField(value = "clicked_count")
    private Integer clickedCount;

    /**
     * 视频关联文件Id
     */
    @TableField(value = "save_id")
    private Long saveId;

    /**
     * 封面关联文件Id
     */
    @TableField(value = "cover_id")
    private Long coverId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}