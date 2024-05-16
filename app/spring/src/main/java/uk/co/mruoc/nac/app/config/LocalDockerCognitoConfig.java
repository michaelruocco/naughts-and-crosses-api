package uk.co.mruoc.nac.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import uk.co.mruoc.nac.usecases.UserProvider;
import uk.co.mruoc.nac.user.cognito.CognitoUserProvider;
import uk.co.mruoc.nac.user.cognito.LocalDockerCognitoConfigurer;

@ConditionalOnProperty(value = "aws.cognito.local.docker", havingValue = "true")
@Configuration
@Slf4j
public class LocalDockerCognitoConfig {

  @Bean
  public UserProvider localDockerCognitoUserProvider(
      CognitoIdentityProviderClient client,
      @Value("${aws.cognito.userPoolId:}") String initialUserPoolId,
      LocalDockerCognitoConfigurer configurer) {
    String userPoolId = configurer.configureAndReplacePoolIdIfRequired(initialUserPoolId);
    log.info("configuring cognito user provider with user pool id {}", userPoolId);
    return CognitoUserProvider.builder().client(client).userPoolId(userPoolId).build();
  }

  @Bean
  public JwtDecoder jwtDecoder(
      @Value("${auth.issuer.url}") String initialIssuerUrl,
      LocalDockerCognitoConfigurer configurer) {
    String issuerUrl = configurer.configureAndReplacePoolIdIfRequired(initialIssuerUrl);
    log.info("configuring jwt decoder with issuer url {}", issuerUrl);
    NimbusJwtDecoder decoder = JwtDecoders.fromOidcIssuerLocation(issuerUrl);
    OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuerUrl);
    OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer);
    decoder.setJwtValidator(withAudience);
    return decoder;
  }

  @Bean
  public LocalDockerCognitoConfigurer localDockerUserPoolConfigurer(
      CognitoIdentityProviderClient client) {
    log.warn("local docker cognito user pool should only be used for local development");
    return LocalDockerCognitoConfigurer.builder().client(client).confirmationCode("9999").build();
  }
}
