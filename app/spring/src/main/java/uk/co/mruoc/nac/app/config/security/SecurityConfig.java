package uk.co.mruoc.nac.app.config.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static uk.co.mruoc.nac.app.config.security.KeycloakAuthIssuerUrlParser.toRealm;
import static uk.co.mruoc.nac.app.config.security.KeycloakAuthIssuerUrlParser.toSchemeHostAndPort;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import uk.co.mruoc.nac.app.config.websocket.AuthChannelInterceptor;
import uk.co.mruoc.nac.app.config.websocket.DefaultAuthChannelInterceptor;
import uk.co.mruoc.nac.usecases.UserProvider;
import uk.co.mruoc.nac.user.keycloak.KeycloakAdminConfig;
import uk.co.mruoc.nac.user.keycloak.KeycloakUserProvider;

@ConditionalOnProperty(value = "auth.security.enabled", havingValue = "true", matchIfMissing = true)
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                    .requestMatchers("/v1/game-events/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .sessionManagement(sm -> sm.sessionCreationPolicy(STATELESS))
        .oauth2ResourceServer(
            rs -> rs.jwt(jwt -> jwt.jwtAuthenticationConverter(new JwtAuthenticationConverter())))
        .build();
  }

  @Bean
  public AuthChannelInterceptor authChannelInterceptor(JwtDecoder jwtDecoder) {
    return new DefaultAuthChannelInterceptor(jwtDecoder);
  }

  @Bean
  public KeycloakAdminConfig keycloakAdminConfig(
      @Value("${auth.issuer.url}") String url,
      @Value("${keycloak.admin.client.id}") String clientId,
      @Value("${keycloak.admin.client.secret}") String clientSecret) {
    return KeycloakAdminConfig.builder()
        .url(toSchemeHostAndPort(url))
        .realm(toRealm(url))
        .clientId(clientId)
        .clientSecret(clientSecret)
        .build();
  }

  @Bean
  public UserProvider keycloakUserProvider(KeycloakAdminConfig config) {
    return new KeycloakUserProvider(config);
  }
}
