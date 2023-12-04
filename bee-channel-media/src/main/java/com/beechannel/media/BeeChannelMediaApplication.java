package com.beechannel.media;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients("com.beechannel.media.feign")
@SpringBootApplication(scanBasePackages = {"com.beechannel.media", "com.beechannel.base"})
@MapperScan("com.beechannel.media.mapper")
public class BeeChannelMediaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeeChannelMediaApplication.class, args);
    }

}
