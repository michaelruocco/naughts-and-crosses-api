package uk.co.mruoc.nac.app.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import uk.co.mruoc.nac.api.converter.ApiConverter;
import uk.co.mruoc.nac.app.websocket.WebSocketGameEventPublisher;
import uk.co.mruoc.nac.usecases.GameEventPublisher;

import java.util.Arrays;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final AllowedOriginsSupplier originsSupplier;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        String[] origins = originsSupplier.get();
        log.info("web socket connections allowed from origins {}", Arrays.toString(origins));
        registry.addEndpoint("/v1/game-events")
                .setAllowedOrigins(origins)
                .withSockJS();
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
