package com.beechannel.auth.domain.dto;

import com.beechannel.base.domain.po.Role;
import com.beechannel.base.domain.po.User;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description user with its roleList
 * @Author eotouch
 * @Date 2023/11/16 21:16
 * @Version 1.0
 */
@Data
public class UserExt extends User implements Serializable {

    private List<Role> roleList;
}
