package com.beechannel.media.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.beechannel.media.domain.po.PlayVideoList;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * sign play video list
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/02 13:51
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SignPlayVideoList extends PlayVideoList {

    @TableField(exist = false)
    private boolean requireDelete;
}
