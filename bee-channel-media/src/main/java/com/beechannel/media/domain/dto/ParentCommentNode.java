package com.beechannel.media.domain.dto;

import com.beechannel.media.domain.po.Comment;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2023/12/03 23:06
 * @Version 1.0
 */
@Data
public class ParentCommentNode extends Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer childrenCount;
}
