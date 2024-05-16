package uk.co.mruoc.nac.user.cognito;

import java.net.URI;
import lombok.Builder;
import lombok.Data;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

@Builder
@Data
public class CognitoClientConfig {

  private final AwsCredentialsProvider credentialsProvider;
  private final URI endpointOverride;
  private final Region region;

  public static CognitoClientConfig defaultTestConfigBuilder() {
    return CognitoClientConfig.builder()
        .credentialsProvider(defaultTestCredentialsProvider())
        .endpointOverride(URI.create("http://localhost:9229"))
        .region(Region.EU_WEST_2)
        .build();
  }

  private static AwsCredentialsProvider defaultTestCredentialsProvider() {
    AwsBasicCredentials credentials =
        AwsBasicCredentials.builder().accessKeyId("abc").secretAccessKey("123").build();
    return StaticCredentialsProvider.create(credentials);
  }
}
