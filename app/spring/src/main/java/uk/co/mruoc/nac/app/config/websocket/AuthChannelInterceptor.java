package uk.co.mruoc.nac.app.config.websocket;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Slf4j
@RequiredArgsConstructor
public class AuthChannelInterceptor implements ChannelInterceptor {

  private final JwtDecoder jwtDecoder;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    if (isConnectMessage(accessor)) {
      log.debug("attempting to authenticate web socket connect message");
      String token = extractAuthorizationToken(accessor);
      Authentication user = toAuthentication(token);
      accessor.setUser(user);
    }
    return message;
  }

  private boolean isConnectMessage(StompHeaderAccessor accessor) {
    return StompCommand.CONNECT.equals(accessor.getCommand());
  }

  private Authentication toAuthentication(String token) {
    try {
      Jwt jwt = jwtDecoder.decode(token);
      Authentication user = new JwtAuthenticationToken(jwt);
      log.debug("authenticated user {}", user.getName());
      return user;
    } catch (JwtException e) {
      log.error(e.getMessage(), e);
      throw new OAuth2AuthenticationException(e.getMessage());
    }
  }

  private static String extractAuthorizationToken(StompHeaderAccessor accessor) {
    return Optional.ofNullable(accessor.getFirstNativeHeader(AUTHORIZATION))
        .orElseThrow(() -> new OAuth2AuthenticationException("authorization header not provided"));
  }
}
