package uk.co.mruoc.nac.app.config.user;

import io.micrometer.common.util.StringUtils;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClientBuilder;
import uk.co.mruoc.nac.usecases.UserProvider;
import uk.co.mruoc.nac.user.cognito.CognitoUserProvider;
import uk.co.mruoc.nac.user.inmemory.StubUserProvider;

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

  @ConditionalOnProperty(value = "aws.cognito.local.docker", havingValue = "false")
  @Bean
  public UserProvider cognitoUserProvider(
      CognitoIdentityProviderClient client, @Value("${aws.cognito.userPoolId}") String userPoolId) {
    log.info("configuring cognito user provider with user pool id {}", userPoolId);
    return CognitoUserProvider.builder().client(client).userPoolId(userPoolId).build();
  }

  @ConditionalOnProperty(value = "stub.user.provider", havingValue = "true")
  @Bean
  public UserProvider stubUserProvider() {
    return new StubUserProvider();
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
