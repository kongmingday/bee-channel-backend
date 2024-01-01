package com.beechannel.media.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description commit comment request param
 * @Author eotouch
 * @Date 2023/12/10 18:06
 * @Version 1.0
 */
@Data
public class CommitCommentParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long deriveId;
    @NotNull
    private Integer deriveType;
    @NotNull
    private String content;
    private Long userToId;
    private Long parentId;
}
