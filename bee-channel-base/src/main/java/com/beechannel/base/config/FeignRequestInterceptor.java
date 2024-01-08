package com.beechannel.base.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @Description multipart support config
 * @Author eotouch
 * @Date 2023/12/22 1:02
 * @Version 1.0
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        RequestAttributes requestAttributes = null;
        try {
            requestAttributes = RequestContextHolder.currentRequestAttributes();
        } catch (Exception e) {
            return;
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String authorization = request.getHeader(AUTHORIZATION);
        template.header(AUTHORIZATION, authorization);
    }

}
