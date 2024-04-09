package com.beechannel.media.service;

import com.beechannel.base.domain.vo.RestResponse;

/**
 * recommend service
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/09 10:08
 */
public interface RecommendService {

    /**
     * recommend video
     *
     * @return RestResponse the recommend result
     * @author eotouch
     * @date 2024-04-09 10:37
     */
    RestResponse recommend(Integer count);
}
