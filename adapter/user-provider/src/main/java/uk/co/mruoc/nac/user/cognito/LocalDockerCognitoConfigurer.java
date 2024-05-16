package uk.co.mruoc.nac.user.cognito;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ConfirmSignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolClientRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolClientResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.CreateUserPoolResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolClientsResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUserPoolsResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.ListUsersResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.SignUpResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.TimeUnitsType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolClientDescription;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserPoolDescriptionType;
import software.amazon.awssdk.services.cognitoidentityprovider.paginators.ListUsersIterable;

@Slf4j
@Builder
public class LocalDockerCognitoConfigurer {

  private static final String POOL_ID_PLACE_HOLDER = "%POOL_ID%";

  private final CognitoIdentityProviderClient client;
  private final String confirmationCode;

  public static void main(String[] args) {
    LocalDockerCognitoConfigurer.builder()
        .client(buildIdentityProviderClient())
        .confirmationCode("9999")
        .build()
        .configure();
  }

  public String configureAndReplacePoolIdIfRequired(String initial) {
    if (initial.contains(POOL_ID_PLACE_HOLDER)) {
      String replaced = initial.replace(POOL_ID_PLACE_HOLDER, configure().getPoolId());
      log.info("replaced initial value {} with {}", initial, replaced);
      return replaced;
    }
    log.info(
        "initial {} does not contain placeholder {} so no replacement required",
        initial,
        POOL_ID_PLACE_HOLDER);
    return initial;
  }

  public CognitoUserPoolAndClientIds configure() {
    String poolId = findOrCreateUserPool();
    String clientId = findOrCreateUserPoolClient(poolId);
    findOrCreateUser(poolId, clientId, user1());
    findOrCreateUser(poolId, clientId, user2());
    return CognitoUserPoolAndClientIds.builder().poolId(poolId).clientId(clientId).build();
  }

  private String findOrCreateUserPool() {
    String poolName = "local-nac-pool";
    return findUserPoolId(poolName).orElseGet(() -> createUserPool(poolName));
  }

  private Optional<String> findUserPoolId(String name) {
    ListUserPoolsRequest request = ListUserPoolsRequest.builder().build();
    ListUserPoolsResponse response = client.listUserPools(request);
    return response.userPools().stream()
        .filter(pool -> pool.name().equals(name))
        .map(UserPoolDescriptionType::id)
        .findFirst();
  }

  private String createUserPool(String poolName) {
    CreateUserPoolRequest request = CreateUserPoolRequest.builder().poolName(poolName).build();
    CreateUserPoolResponse response = client.createUserPool(request);
    String poolId = response.userPool().id();
    log.info("created user pool with id " + poolId + " and name " + poolName);
    return poolId;
  }

  private String findOrCreateUserPoolClient(String poolId) {
    String clientName = "local-nac-pool-client";
    return findUserPoolClientId(poolId, clientName)
        .orElseGet(() -> createUserPoolClient(poolId, clientName));
  }

  private Optional<String> findUserPoolClientId(String poolId, String clientName) {
    ListUserPoolClientsRequest request =
        ListUserPoolClientsRequest.builder().userPoolId(poolId).build();
    ListUserPoolClientsResponse response = client.listUserPoolClients(request);
    return response.userPoolClients().stream()
        .filter(poolClient -> poolClient.clientName().equals(clientName))
        .map(UserPoolClientDescription::clientId)
        .findFirst();
  }

  private String createUserPoolClient(String poolId, String clientName) {
    CreateUserPoolClientRequest request =
        CreateUserPoolClientRequest.builder()
            .clientName(clientName)
            .userPoolId(poolId)
            .tokenValidityUnits(u -> u.accessToken(TimeUnitsType.HOURS))
            .accessTokenValidity(1)
            .build();
    CreateUserPoolClientResponse response = client.createUserPoolClient(request);
    String clientId = response.userPoolClient().clientId();
    log.info(
        "created user pool client with id {} with name {} in pool with id {}",
        clientId,
        clientName,
        poolId);
    return clientId;
  }

  private void findOrCreateUser(String poolId, String clientId, UserParams params) {
    if (userExists(poolId, params)) {
      return;
    }
    createUser(clientId, params);
  }

  private boolean userExists(String poolId, UserParams params) {
    ListUsersRequest request = ListUsersRequest.builder().userPoolId(poolId).build();
    ListUsersIterable responses = client.listUsersPaginator(request);
    return responses.stream()
        .map(ListUsersResponse::users)
        .flatMap(Collection::stream)
        .anyMatch(user -> user.username().equals(params.getUsername()));
  }

  private void createUser(String clientId, UserParams params) {
    signUp(clientId, params);
    confirmSignUp(clientId, params.getUsername());
  }

  private void signUp(String clientId, UserParams params) {
    String username = params.getUsername();
    SignUpRequest signUpRequest =
        SignUpRequest.builder()
            .clientId(clientId)
            .username(username)
            .password(params.getPassword())
            .userAttributes(toAttributes(params))
            .build();
    SignUpResponse response = client.signUp(signUpRequest);
    log.info("user {} has been signed up with subject {}", username, response.userSub());
  }

  private void confirmSignUp(String clientId, String username) {
    ConfirmSignUpRequest signUpRequest =
        ConfirmSignUpRequest.builder()
            .clientId(clientId)
            .username(username)
            .confirmationCode(confirmationCode)
            .build();
    ConfirmSignUpResponse response = client.confirmSignUp(signUpRequest);
    log.debug("user {} confirmed {}", username, response.sdkHttpResponse().isSuccessful());
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

  private static List<AttributeType> toAttributes(UserParams params) {
    return List.of(
        subjectAttribute(params.getSubject()),
        givenNameAttribute(params.getGivenName()),
        familyNameAttribute(params.getFamilyName()),
        emailAttribute(params.getEmail()));
  }

  private static AttributeType subjectAttribute(String givenName) {
    return AttributeType.builder().name("sub").value(givenName).build();
  }

  private static AttributeType givenNameAttribute(String givenName) {
    return AttributeType.builder().name("given_name").value(givenName).build();
  }

  private static AttributeType familyNameAttribute(String familyName) {
    return AttributeType.builder().name("family_name").value(familyName).build();
  }

  private static AttributeType emailAttribute(String email) {
    return AttributeType.builder().name("email").value(email).build();
  }

  private static UserParams user1() {
    return UserParams.builder()
        .subject("707d9fa6-13dd-4985-93aa-a28f01e89a6b")
        .username("user-1")
        .password("pwd1")
        .givenName("User")
        .familyName("One")
        .email("user-1@email.com")
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
        .build();
  }

  @Builder
  @Data
  public static class UserParams {
    private final String username;
    private final String password;
    private final String subject;
    private final String givenName;
    private final String familyName;
    private final String email;
  }
}
