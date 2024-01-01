package com.beechannel.media.constant;

import lombok.Getter;

/**
 * @Description file type
 * @Author eotouch
 * @Date 2023/12/18 17:15
 * @Version 1.0
 */
@Getter
public enum FileType {

    IMAGE("image"),
    MEDIA("media");

    private final String name;

    FileType(String name) {
        this.name = name;
    }

}
