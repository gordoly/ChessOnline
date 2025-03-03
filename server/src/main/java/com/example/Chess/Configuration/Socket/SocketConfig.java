package com.example.Chess.Configuration.Socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class SocketConfig {
    /**
     * Creates and returns a {@link ServerEndpointExporter} bean.
     * 
     * The {@link ServerEndpointExporter} bean is responsible for enabling WebSocket 
     * endpoints to be registered and handled by the Spring container.
     * 
     * @return a {@link ServerEndpointExporter} instance to enable WebSocket endpoints
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}