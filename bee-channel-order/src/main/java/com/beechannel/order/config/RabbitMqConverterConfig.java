package com.beechannel.order.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description rabbit-mq serializable converter configuration
 * @Author eotouch
 * @Date 2024/01/06 21:02
 * @Version 1.0
 */
@Configuration
public class RabbitMqConverterConfig {

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
