package com.beechannel.live.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author Destiny
 * @date 2022/12/16
 */
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {

    /**
     * @description set HttpServlet before the websocket connect;
     * @param sec
     * @param request
     * @param response
     * @return void
     * @author eotouch
     * @date 2024-01-06 23:00
     */
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        // get authorization
        List<String> authorization = request.getHeaders().get(AUTHORIZATION);
        if(authorization != null && !authorization.isEmpty()){
            // set mock HttpServletRequest to call feign service
            MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
            mockHttpServletRequest.addHeader(AUTHORIZATION, authorization.get(0));
            ServletRequestAttributes servletRequestAttributes = new ServletRequestAttributes(mockHttpServletRequest);
            RequestContextHolder.setRequestAttributes(servletRequestAttributes);
        }
        super.modifyHandshake(sec, request, response);
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter(){
        return new ServerEndpointExporter();
    }
}
