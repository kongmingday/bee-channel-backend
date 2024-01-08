package com.beechannel.user.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description file upload result
 * @Author eotouch
 * @Date 2024/01/03 17:01
 * @Version 1.0
 */
@Data
public class FileUploadResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long fileId;
    private String filePath;
}
