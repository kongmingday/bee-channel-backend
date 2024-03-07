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
 * @TableName media_file
 */
@TableName(value ="media_file")
@Data
public class MediaFile implements Serializable {
    /**
     * 文件表主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 上传用户Id
     */
    @TableField(value = "upload_user")
    private Long uploadUser;

    /**
     * 文件桶目录
     */
    @TableField(value = "bucket")
    private String bucket;

    /**
     * 文件存储路径
     */
    @TableField(value = "file_path")
    private String filePath;

    /**
     * 文件上传日期
     */
    @TableField(value = "upload_time")
    private LocalDateTime uploadTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}