package com.beechannel.media.constant;

import lombok.Getter;

/**
 * @Description search sort type
 * @Author eotouch
 * @Date 2024/01/10 15:27
 * @Version 1.0
 */
@Getter
public enum SearchSortType {

    MOST(0, "most"),
    NEWEST(1, "newest");

    SearchSortType(int id, String description) {
        this.id = id;
        this.description = description;
    }

    private int id;
    private String description;
}
