package com.beechannel.base.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description multipart support config
 * @Author eotouch
 * @Date 2023/12/22 1:02
 * @Version 1.0
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    private final static String AUTHORIZATION = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String authorization = request.getHeader(AUTHORIZATION);
        template.header(AUTHORIZATION, authorization);
    }

}
