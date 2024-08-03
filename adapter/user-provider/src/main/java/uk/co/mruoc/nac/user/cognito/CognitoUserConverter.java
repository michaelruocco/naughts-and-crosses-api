package uk.co.mruoc.nac.user.cognito;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserName;

public class CognitoUserConverter {

  private static final String SUB = "sub";
  private static final String NAME = "name";
  private static final String GIVEN_NAME = "given_name";
  private static final String FAMILY_NAME = "family_name";
  private static final String EMAIL = "email";
  private static final String EMAIL_VERIFIED = "email_verified";

  public String toUsernameFilter(String username) {
    return String.format("username = \"%s\"", username);
  }

  public User toUser(UserType user, Map<String, Collection<String>> usernamesAndGroups) {
    return toUser(user, usernamesAndGroups.getOrDefault(user.username(), Collections.emptySet()));
  }

  public User toUser(UserType user, Collection<String> groups) {
    Map<String, String> attributes = toMap(user.attributes());
    return User.builder()
        .id(attributes.get(SUB))
        .username(user.username())
        .name(toName(attributes))
        .email(attributes.get(EMAIL))
        .emailVerified(Boolean.parseBoolean(attributes.get(EMAIL_VERIFIED)))
        .groups(groups)
        .status(user.userStatusAsString())
        .build();
  }

  public Collection<AttributeType> toAttributes(UpsertUserRequest request) {
    return List.of(
        toNameAttribute(request.getFullName()),
        toGivenNameAttribute(request.getFirstName()),
        toFamilyNameAttribute(request.getLastName()),
        toEmailAttribute(request.getEmail()),
        toEmailVerifiedAttribute(request.isEmailVerified()));
  }

  public Collection<AttributeType> toAttributes(User user) {
    return List.of(
        toNameAttribute(user.getFullName()),
        toGivenNameAttribute(user.getFirstName()),
        toFamilyNameAttribute(user.getLastName()),
        toEmailAttribute(user.getEmail()),
        toEmailVerifiedAttribute(user.isEmailVerified()));
  }

  private static UserName toName(Map<String, String> attributes) {
    return UserName.builder()
        .full(attributes.get(NAME))
        .first(attributes.get(GIVEN_NAME))
        .last(attributes.get(FAMILY_NAME))
        .build()
        .tryToPopulateAll();
  }

  private static AttributeType toNameAttribute(String value) {
    return AttributeType.builder().name(NAME).value(value).build();
  }

  private static AttributeType toGivenNameAttribute(String value) {
    return AttributeType.builder().name(GIVEN_NAME).value(value).build();
  }

  private static AttributeType toFamilyNameAttribute(String value) {
    return AttributeType.builder().name(FAMILY_NAME).value(value).build();
  }

  private static AttributeType toEmailAttribute(String value) {
    return AttributeType.builder().name(EMAIL).value(value).build();
  }

  private static AttributeType toEmailVerifiedAttribute(boolean verified) {
    return AttributeType.builder().name(EMAIL_VERIFIED).value(Boolean.toString(verified)).build();
  }

  private static Map<String, String> toMap(List<AttributeType> attributes) {
    return attributes.stream().collect(Collectors.toMap(AttributeType::name, AttributeType::value));
  }
}
