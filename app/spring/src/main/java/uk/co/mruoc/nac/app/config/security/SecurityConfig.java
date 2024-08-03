package uk.co.mruoc.nac.app.config.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.extern.slf4j.Slf4j;
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
import uk.co.mruoc.nac.app.security.SpringAuthenticatedUserSupplier;
import uk.co.mruoc.nac.usecases.AuthCodeClient;
import uk.co.mruoc.nac.usecases.AuthService;
import uk.co.mruoc.nac.usecases.AuthenticatedUserSupplier;
import uk.co.mruoc.nac.usecases.AuthenticatedUserValidator;
import uk.co.mruoc.nac.usecases.ExternalUserPresentRetry;
import uk.co.mruoc.nac.usecases.ExternalUserService;
import uk.co.mruoc.nac.usecases.ExternalUserSynchronizer;
import uk.co.mruoc.nac.usecases.TokenService;
import uk.co.mruoc.nac.usecases.UserFinder;

@Configuration
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.ignoringRequestMatchers("/v1/auth/**"))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                    .requestMatchers("/v1/auth/**", "/v1/game-events/**", "/actuator/info")
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
  public AuthenticatedUserSupplier authenticatedUserSupplier(UserFinder userFinder) {
    return new SpringAuthenticatedUserSupplier(userFinder);
  }

  @Bean
  public AuthenticatedUserValidator authenticatedUserValidator(AuthenticatedUserSupplier supplier) {
    return new AuthenticatedUserValidator(supplier);
  }

  @Bean
  public AuthService authService(
      TokenService tokenService,
      AuthCodeClient authCodeClient,
      ExternalUserService externalUserService,
      ExternalUserSynchronizer synchronizer) {
    return AuthService.builder()
        .tokenService(tokenService)
        .authCodeClient(authCodeClient)
        .externalUserPresentRetry(new ExternalUserPresentRetry(externalUserService))
        .synchronizer(synchronizer)
        .build();
  }
}
