package com.beechannel.base.domain.po;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 用户Id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户姓名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 用户密码
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @TableField(value = "password")
    private String password;

    /**
     * 微信用户标识
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @TableField(value = "wx_union_id")
    private String wxUnionId;

    /**
     * 用户头像
     */
    @TableField(value = "profile")
    private String profile;

    /**
     * 用户首页背景
     */
    @TableField(value = "background")
    private String background;

    /**
     * 用户简介
     */
    @TableField(value = "introduction")
    private String introduction;

    /**
     * 用户生日
     */
    @TableField(value = "birthday")
    private LocalDateTime birthday;

    /**
     * 用户性别 0-female 1-male
     */
    @TableField(value = "gender")
    private Integer gender;

    /**
     * 用户邮箱
     */
    @TableField(value = "email")
    private String email;

    /**
     * 用户电话
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 用户创建日期
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 账号状态 0-封禁 1-禁止评论 2-正常状态
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}