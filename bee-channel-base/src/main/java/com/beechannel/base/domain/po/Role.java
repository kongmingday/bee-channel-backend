package com.beechannel.base.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Role implements Serializable {

    /**
    * 角色Id
    */
    @ApiModelProperty("角色Id")
    private Integer id;
    /**
    * 角色名称
    */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
    * 角色编码
    */
    @ApiModelProperty("角色编码")
    private String roleCode;

    /**
    * 角色创建时间
    */
    @ApiModelProperty("角色创建时间")
    private LocalDateTime createTime;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
