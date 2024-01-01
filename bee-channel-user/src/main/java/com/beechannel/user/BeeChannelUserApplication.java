package com.beechannel.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(scanBasePackages = {"com.beechannel.user", "com.beechannel.base"} )
@MapperScan("com.beechannel.user.mapper")
@EnableFeignClients(basePackages = "com.beechannel.user.feign")
public class BeeChannelUserApplication {

    @Bean
    RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory());
        return  restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(BeeChannelUserApplication.class, args);
    }

}
