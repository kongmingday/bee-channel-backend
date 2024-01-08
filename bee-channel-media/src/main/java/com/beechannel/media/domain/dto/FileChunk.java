package com.beechannel.media.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description file chunk dto
 * @Author eotouch
 * @Date 2023/12/31 17:31
 * @Version 1.0
 */
@Data
public class FileChunk {

    private String fileHash;

    private String index;

    private MultipartFile chunk;
}
