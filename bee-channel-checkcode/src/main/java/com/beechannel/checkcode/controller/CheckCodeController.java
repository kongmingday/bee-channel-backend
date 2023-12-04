package com.beechannel.checkcode.controller;

import com.beechannel.checkcode.model.CheckCodeParamsDto;
import com.beechannel.checkcode.model.CheckCodeResultDto;
import com.beechannel.checkcode.service.CheckCodeService;
import com.beechannel.checkcode.service.EmailService;
import com.beechannel.checkcode.service.impl.NumberLetterCheckCodeGenerator;
import com.beechannel.checkcode.service.RedisCheckCodeStore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author eotouch
 * @version 1.0
 * @description verify code service
 * @date 2022/9/29 18:39
 */
@Api(value = "验证码服务接口")
@RestController
public class CheckCodeController {

    @Resource(name = "PicCheckCodeService")
    private CheckCodeService picCheckCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NumberLetterCheckCodeGenerator numberCodeGenerator;

    @Resource(name = "RedisCheckCodeStore")
    private RedisCheckCodeStore redisCheckCodeStore;

    @Value("${spring.mail.username}")
    private String sendEmail;

    @Value("${bee-channel.front-url}")
    private String frontUrl;

    private static final String SUBJECT = "bee-channel website message";

    private static final String SEND_KEY = "sendKey";

    @ApiOperation(value = "generate verify code", notes = "generate verify code")
    @PostMapping(value = "/pic")
    public CheckCodeResultDto generatePicCheckCode(CheckCodeParamsDto checkCodeParamsDto) {
        return picCheckCodeService.generate(checkCodeParamsDto);
    }

    @ApiOperation(value = "generate verify code", notes = "generate verify code")
    @PostMapping(value = "sendCode")
    public void sendCheckCode(String param) {
        if (param.contains("@")) {
            String generate = numberCodeGenerator.generate(8);
            emailService.sendSimpleMail(sendEmail, param,
                    SUBJECT,
                    "your verify code is: " + generate);
            redisCheckCodeStore.set(SEND_KEY + ":" + param, generate, 180);
        } else {
            return;
        }
    }

    @ApiOperation(value = "enable your account", notes = "enable your account")
    @PostMapping(value = "enableAccount")
    public void enableAccount(String email) {
        String generate = numberCodeGenerator.generate(8);
        String urlPattern = "http://%s?email=%s&token=%s";
        String contentPattern = "enable your account, please click this url: %s";

        String url = String.format(urlPattern, frontUrl, email, generate);
        String content = String.format(contentPattern, url);

        emailService.sendSimpleMail(sendEmail, email,
                SUBJECT,
                content);
        redisCheckCodeStore.set(SEND_KEY + ":" + email, generate, 180);
    }

    @ApiOperation(value = "verify", notes = "verify")
    @PostMapping(value = "/verify")
    public Boolean verify(String key, String code) {
        Boolean isSuccess = picCheckCodeService.verify(key, code);
        return isSuccess;
    }
}
