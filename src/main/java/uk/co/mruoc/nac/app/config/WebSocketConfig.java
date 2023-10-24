package uk.co.mruoc.nac.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import uk.co.mruoc.nac.app.api.converter.ApiConverter;
import uk.co.mruoc.nac.app.domain.usecases.GameEventPublisher;
import uk.co.mruoc.nac.app.websocket.WebSocketGameEventPublisher;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/v1/game-events").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/topic");
    }

    @Bean
    public GameEventPublisher gameEventPublisher(SimpMessagingTemplate template, ApiConverter converter) {
        return WebSocketGameEventPublisher.builder()
                .template(template)
                .converter(converter)
                .build();
    }
}
