package com.beechannel.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.beechannel.order", "com.beechannel.base"})
@MapperScan("com.beechannel.order.mapper")
@EnableFeignClients("com.beechannel.order.feign")
public class BeeChannelOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeeChannelOrderApplication.class, args);
    }

}
