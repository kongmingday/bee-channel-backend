package com.beechannel.base.domain.dto;

import com.alibaba.fastjson.JSON;
import com.beechannel.base.constant.UserStatus;
import com.beechannel.base.domain.po.Role;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.exception.BeeChannelException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description implement UserDetails to overlap custom
 * @Author eotouch
 * @Date 2023/11/16 0:24
 * @Version 1.0
 */
@Data
public class UserDetail implements UserDetails {

    private User user;

    private List<Role> roleList;

    private List<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(authorities == null){
            authorities = roleList.stream().map(Role::getRoleName)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return JSON.toJSONString(user);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return UserStatus.ENABLE.getCode().equals(user.getStatus());
    }
}
