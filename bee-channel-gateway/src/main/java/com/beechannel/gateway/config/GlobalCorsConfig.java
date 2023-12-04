package com.beechannel.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @Description resolve cors problem
 * @Author eotouch
 * @Date 2023/11/20 23:29
 * @Version 1.0
 */

@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // allow origin
        config.addAllowedOrigin("*");
        // allow header
        config.addAllowedHeader("*");
        // allow request header
        config.addAllowedMethod("*");
        // expose header
        config.addExposedHeader("*");
        // allow cookie
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsWebFilter(source);
    }
}
