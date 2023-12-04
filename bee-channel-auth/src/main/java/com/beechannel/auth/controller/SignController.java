package com.beechannel.auth.controller;

import com.beechannel.auth.domain.dto.SignUpParams;
import com.beechannel.auth.mapper.UserMapper;
import com.beechannel.auth.service.UserService;
import com.beechannel.auth.service.WeChatAuthService;
import com.beechannel.base.domain.po.User;
import com.beechannel.base.domain.vo.RestResponse;
import io.swagger.annotations.Api;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Description TODO
 * @Author eotouch
 * @Date 2023/11/20 15:02
 * @Version 1.0
 */
@Controller
public class SignController {

    @Resource
    private WeChatAuthService wxAuthService;

    @Resource
    private UserService userService;

    @RequestMapping("/WeChatLogin")
    public String WeChatLogin(String code) throws IOException {
        User user = wxAuthService.wechatAuth(code);
        if (user == null) {
            return "redirect:http://localhost:3000";
        }
        String unionId = user.getWxUnionId();
        return "redirect:http://localhost:3000/loading-section?unionId=" + unionId + "&authType=WeChat";
    }

    @ResponseBody
    @PostMapping("/signUp")
    public RestResponse register(@RequestBody SignUpParams params){
        return userService.register(params);
    }

    @ResponseBody
    @PostMapping("/enable")
    public boolean enable(String email, String token){
        return userService.enableAccount(email, token);
    }
}
