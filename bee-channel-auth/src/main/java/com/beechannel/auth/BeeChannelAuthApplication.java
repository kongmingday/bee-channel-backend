package com.beechannel.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"com.beechannel.auth", "com.beechannel.base"})
@MapperScan("com.beechannel.auth.mapper")
@EnableFeignClients(basePackages = "com.beechannel.auth.feign")
public class BeeChannelAuthApplication {

    @Bean
    RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        return  restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(BeeChannelAuthApplication.class, args);
    }

}
