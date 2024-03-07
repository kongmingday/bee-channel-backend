package com.beechannel.base.domain.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Description search param
 * @Author eotouch
 * @Date 2024/01/10 15:24
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SearchParams extends PageParams implements Serializable {

    private static final long serialVersionUID = 1L;

    private String keyword;

    private Integer categoryId;

    private Integer sortBy;
}
