package com.beechannel.live.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * the information of the active live
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/10 11:20
 */
@Data
public class ActiveLiveInfo implements Serializable {



    /**
     * live id
     */
    private String id;

    /**
     * live title
     */
    private String title;

    /**
     * live cover
     */
    private String cover;

    /**
     * live introduction
     */
    private String introduction;

    /**
     * live user' id
     */
    private Long userId;
}
