package uk.co.mruoc.nac.app.config.websocket;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import uk.co.mruoc.nac.api.converter.ApiConverter;
import uk.co.mruoc.nac.app.config.AllowedOriginsSupplier;
import uk.co.mruoc.nac.app.websocket.WebSocketGameEventPublisher;
import uk.co.mruoc.nac.usecases.GameEventPublisher;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final AllowedOriginsSupplier originsSupplier;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    String[] origins = originsSupplier.get();
    log.info("web socket connections allowed from origins {}", Arrays.toString(origins));
    registry.addEndpoint("/v1/game-events").setAllowedOrigins(origins).withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes("/app");
    registry.enableSimpleBroker("/topic");
  }

  @Bean
  public GameEventPublisher webSocketGameEventPublisher(
      SimpMessagingTemplate template, ApiConverter converter) {
    return WebSocketGameEventPublisher.builder().template(template).converter(converter).build();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(new AuthChannelInterceptor());
  }
}
