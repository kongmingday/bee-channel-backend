package com.beechannel.media.config;

import com.beechannel.media.service.CensorshipService;
import com.beechannel.media.service.impl.DefaultCensorshipImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * default censorship configuration
 *
 * @author eotouch
 * @version 1.0
 * @date 2024/04/10 14:10
 */
@Configuration
public class DefaultCensorshipConfig {

    @ConditionalOnMissingBean(CensorshipService.class)
    @Bean
    public CensorshipService censorshipService(){
        return new DefaultCensorshipImpl();
    }
}
