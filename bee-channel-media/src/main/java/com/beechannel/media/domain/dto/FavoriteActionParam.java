package com.beechannel.media.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Description the favorite action's params
 * @Author eotouch
 * @Date 2023/12/08 16:55
 * @Version 1.0
 */
@Data
public class FavoriteActionParam {

    @NotNull
    private Long sourceId;
    @NotNull
    private Integer deriveType;
    @NotNull
    private Integer favoriteType;
    private Long userToId;
}
