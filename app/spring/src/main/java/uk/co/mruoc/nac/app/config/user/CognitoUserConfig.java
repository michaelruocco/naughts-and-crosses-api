package uk.co.mruoc.nac.app.config.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.client.RestTemplate;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClientBuilder;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;
import uk.co.mruoc.nac.usecases.AuthCodeClient;
import uk.co.mruoc.nac.usecases.ExternalUserService;
import uk.co.mruoc.nac.usecases.TokenService;
import uk.co.mruoc.nac.user.JwtParser;
import uk.co.mruoc.nac.user.cognito.CognitoAuthCodeClient;
import uk.co.mruoc.nac.user.cognito.CognitoGroupService;
import uk.co.mruoc.nac.user.cognito.CognitoTokenService;
import uk.co.mruoc.nac.user.cognito.CognitoUserConverter;
import uk.co.mruoc.nac.user.cognito.CognitoUserService;
import uk.co.mruoc.nac.user.inmemory.StubAuthCodeClient;
import uk.co.mruoc.nac.user.inmemory.StubExternalUserService;
import uk.co.mruoc.nac.user.inmemory.StubJwtParser;
import uk.co.mruoc.nac.user.inmemory.StubTokenService;

@Configuration
@Slf4j
public class CognitoUserConfig {

  @Bean
  public CognitoIdentityProviderClient cognitoIdentityProviderClient(
      @Value("${aws.cognito.regionName:eu-central-1}") String regionName,
      @Value("${aws.cognito.endpointOverride:}") String endpointOverride,
      @Value("${aws.cognito.accessKeyId:}") String accessKeyId,
      @Value("${aws.cognito.secretAccessKey:}") String secretAccessKey) {
    CognitoIdentityProviderClientBuilder builder =
        CognitoIdentityProviderClient.builder()
            .region(Region.of(regionName))
            .credentialsProvider(toCredentialsProvider(accessKeyId, secretAccessKey));
    if (!StringUtils.isEmpty(endpointOverride)) {
      builder.endpointOverride(URI.create(endpointOverride));
    }
    return builder.build();
  }

  @ConditionalOnProperty(
      value = "stub.token.service.enabled",
      havingValue = "false",
      matchIfMissing = true)
  @Bean
  public TokenService cognitoTokenService(
      CognitoIdentityProviderClient client,
      @Value("${aws.cognito.userPoolId}") String userPoolId,
      @Value("${aws.cognito.userPoolClientId}") String userPoolClientId,
      Clock clock,
      JwtParser jwtParser) {
    return CognitoTokenService.builder()
        .client(client)
        .userPoolId(userPoolId)
        .clientId(userPoolClientId)
        .clock(clock)
        .jwtParser(jwtParser)
        .build();
  }

  @Bean
  @ConditionalOnProperty(
      value = "stub.jwt.parser.enabled",
      havingValue = "false",
      matchIfMissing = true)
  public JwtParser springJwtParser(JwtDecoder jwtDecoder) {
    log.info("spring jwt parser configured");
    return new SpringJwtParser(jwtDecoder);
  }

  @Bean
  @ConditionalOnProperty(value = "stub.jwt.parser.enabled", havingValue = "true")
  public JwtParser stubJwtParser(ObjectMapper mapper) {
    log.warn("stub jwt parser configured, this should only be used for local testing");
    return new StubJwtParser(new JacksonJsonConverter(mapper));
  }

  @ConditionalOnProperty(
      value = "stub.auth.code.client.enabled",
      havingValue = "false",
      matchIfMissing = true)
  @Bean
  public AuthCodeClient cognitoAuthCodeClient(
      @Value("${aws.cognito.authCodeEndpoint}") String endpoint,
      @Value("${aws.cognito.userPoolClientId}") String clientId,
      JwtParser jwtParser) {
    log.info("configuring auth code client with endpoint {}", endpoint);
    return CognitoAuthCodeClient.builder()
        .uri(URI.create(endpoint))
        .clientId(clientId)
        .template(new RestTemplate())
        .jwtParser(jwtParser)
        .build();
  }

  @ConditionalOnProperty(value = "stub.auth.code.client.enabled", havingValue = "true")
  @Bean
  public AuthCodeClient stubAuthCodeClient(Clock clock, Supplier<UUID> uuidSupplier) {
    log.warn("stub auth code client configured");
    return new StubAuthCodeClient(clock, uuidSupplier);
  }

  @ConditionalOnProperty(value = "stub.token.service.enabled", havingValue = "true")
  @Bean
  public TokenService stubTokenService(
      Clock clock, Supplier<UUID> uuidSupplier, JwtParser jwtParser) {
    return new StubTokenService(clock, uuidSupplier, jwtParser);
  }

  @ConditionalOnProperty(
      value = "stub.user.provider.enabled",
      havingValue = "false",
      matchIfMissing = true)
  @Bean
  public ExternalUserService externalUserService(
      CognitoIdentityProviderClient client, @Value("${aws.cognito.userPoolId}") String userPoolId) {
    log.info("configuring cognito user provider with user pool id {}", userPoolId);
    CognitoGroupService groupService =
        CognitoGroupService.builder().client(client).userPoolId(userPoolId).build();
    return CognitoUserService.builder()
        .client(client)
        .userPoolId(userPoolId)
        .converter(new CognitoUserConverter())
        .groupService(groupService)
        .build();
  }

  @ConditionalOnProperty(value = "stub.user.provider.enabled", havingValue = "true")
  @Bean
  public ExternalUserService stubExternalUserService(Supplier<UUID> uuidSupplier) {
    return new StubExternalUserService(uuidSupplier);
  }

  private static AwsCredentialsProvider toCredentialsProvider(
      String accessKeyId, String secretAccessKey) {
    if (StringUtils.isEmpty(accessKeyId) || StringUtils.isEmpty(secretAccessKey)) {
      log.info("using default credentials provider");
      return DefaultCredentialsProvider.create();
    }
    return toStaticCredentialsProvider(accessKeyId, secretAccessKey);
  }

  private static AwsCredentialsProvider toStaticCredentialsProvider(
      String accessKeyId, String secretAccessKey) {
    log.warn("static aws basic credentials should only be used for local development");
    AwsBasicCredentials credentials =
        AwsBasicCredentials.builder()
            .accessKeyId(accessKeyId)
            .secretAccessKey(secretAccessKey)
            .build();
    return StaticCredentialsProvider.create(credentials);
  }
}
