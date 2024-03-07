package com.beechannel.base.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author eotouch
 * @date 2023/01/21
 */
@Data
@NoArgsConstructor
public class PageResult<T> implements Serializable {

    // 数据列表
    private T data;

    //总记录数
    private int total;

    public PageResult(T data, int total) {
        this.data = data;
        this.total = total;
    }

}
