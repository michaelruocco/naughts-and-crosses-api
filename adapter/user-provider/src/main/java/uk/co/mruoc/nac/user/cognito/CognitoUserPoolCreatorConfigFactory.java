package uk.co.mruoc.nac.user.cognito;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.Data;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import uk.co.mruoc.nac.user.cognito.CognitoUserPoolCreatorConfig.CognitoUserPoolCreatorConfigBuilder;
import uk.co.mruoc.nac.user.cognito.CognitoUserPoolCreatorConfig.UserParams;

@Data
public class CognitoUserPoolCreatorConfigFactory {

  // TODO remove this method when extracting this class into a library
  // use this method as an example of how to run against a running
  // docker container
  public static void main(String[] args) {
    CognitoUserPoolCreatorConfigFactory factory = new CognitoUserPoolCreatorConfigFactory();
    CognitoUserPoolCreator creator = new CognitoUserPoolCreator(factory.build());
    creator.create();
  }

  public CognitoUserPoolCreatorConfig build() {
    return builder().build();
  }

  public CognitoUserPoolCreatorConfigBuilder builder() {
    return CognitoUserPoolCreatorConfig.builder()
        .client(buildIdentityProviderClient())
        .userPoolName("nac-user-pool")
        .userPoolClientName("nac-user-pool-client")
        .confirmationCode("9999")
        .userParams(buildUserParams())
        .poolIdPlaceHolder("%POOL_ID%");
  }

  private static CognitoIdentityProviderClient buildIdentityProviderClient() {
    return CognitoIdentityProviderClient.builder()
        .credentialsProvider(credentialsProvider())
        .endpointOverride(URI.create("http://localhost:9229"))
        .region(Region.EU_WEST_2)
        .build();
  }

  private static AwsCredentialsProvider credentialsProvider() {
    AwsBasicCredentials credentials =
        AwsBasicCredentials.builder().accessKeyId("abc").secretAccessKey("123").build();
    return StaticCredentialsProvider.create(credentials);
  }

  private static Collection<UserParams> buildUserParams() {
    return List.of(user1(), user2());
  }

  private static UserParams user1() {
    return UserParams.builder()
        .subject("707d9fa6-13dd-4985-93aa-a28f01e89a6b")
        .username("user-1")
        .password("pwd1")
        .givenName("User")
        .familyName("One")
        .email("user-1@email.com")
        .emailVerified(true)
        .groups(Collections.emptyList())
        .build();
  }

  private static UserParams user2() {
    return UserParams.builder()
        .subject("dadfde25-9924-4982-802d-dfd0bce2218d")
        .username("user-2")
        .password("pwd2")
        .givenName("User")
        .familyName("Two")
        .email("user-2@email.com")
        .emailVerified(true)
        .groups(Collections.emptyList())
        .build();
  }
}
