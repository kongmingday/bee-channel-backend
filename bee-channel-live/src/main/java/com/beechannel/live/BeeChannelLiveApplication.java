package com.beechannel.live;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.beechannel.live", "com.beechannel.base"})
@MapperScan("com.beechannel.live.mapper")
@EnableFeignClients("com.beechannel.live.feign")
public class BeeChannelLiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeeChannelLiveApplication.class, args);
    }

}
