package uk.co.mruoc.nac.app.config.websocket;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.StompBrokerRelayRegistration;
import org.springframework.messaging.simp.stomp.StompReactorNettyCodec;
import org.springframework.messaging.tcp.TcpOperations;
import org.springframework.messaging.tcp.reactor.ReactorNettyTcpClient;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import reactor.netty.tcp.TcpClient;
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

  private final BrokerConfig brokerConfig;
  private final AllowedOriginsSupplier originsSupplier;
  private final AuthChannelInterceptor authChannelInterceptor;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    String[] origins = originsSupplier.get();
    log.info("web socket connections allowed from origins {}", Arrays.toString(origins));
    registry.addEndpoint("/v1/game-events").setAllowedOrigins(origins).withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.setApplicationDestinationPrefixes("/app");
    if (brokerConfig.isInMemoryEnabled()) {
      registry.enableSimpleBroker("/topic");
      return;
    }
    configureExternalBroker(registry);
  }

  private void configureExternalBroker(MessageBrokerRegistry registry) {
    StompBrokerRelayRegistration registration =
        registry
            .enableStompBrokerRelay("/topic")
            .setRelayHost(brokerConfig.getHost())
            .setRelayPort(brokerConfig.getPort())
            .setClientLogin(brokerConfig.getClientLogin())
            .setClientPasscode(brokerConfig.getClientPasscode())
            .setSystemLogin(brokerConfig.getSystemLogin())
            .setSystemPasscode(brokerConfig.getSystemPasscode());
    if (brokerConfig.isSslEnabled()) {
      log.info("broker ssl enabled");
      registration.setTcpClient(buildSslTcpClient());
    } else {
      log.warn(
          "broker config ssl is disabled this should only be used for non production environments");
    }
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(authChannelInterceptor);
  }

  @Bean
  public GameEventPublisher webSocketGameEventPublisher(
      SimpMessagingTemplate template, ApiConverter converter) {
    return WebSocketGameEventPublisher.builder().template(template).converter(converter).build();
  }

  private TcpOperations<byte[]> buildSslTcpClient() {
    TcpClient client =
        TcpClient.create().host(brokerConfig.getHost()).port(brokerConfig.getPort()).secure();
    return new ReactorNettyTcpClient<>(client, new StompReactorNettyCodec());
  }
}
