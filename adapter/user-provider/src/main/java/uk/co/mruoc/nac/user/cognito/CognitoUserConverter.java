package uk.co.mruoc.nac.user.cognito;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.User;

public class CognitoUserConverter {

  private static final String SUB = "sub";
  private static final String GIVEN_NAME = "given_name";
  private static final String FAMILY_NAME = "family_name";
  private static final String EMAIL = "email";
  private static final String EMAIL_VERIFIED = "email_verified";

  public String toSubFilter(String id) {
    return String.format("%s = \"%s\"", SUB, id);
  }

  public User toUser(UserType user) {
    Map<String, String> attributes = toMap(user.attributes());
    return User.builder()
        .id(attributes.get(SUB))
        .username(user.username())
        .firstName(attributes.get(GIVEN_NAME))
        .lastName(attributes.get(FAMILY_NAME))
        .email(attributes.get(EMAIL))
        .emailVerified(Boolean.parseBoolean(attributes.get(EMAIL_VERIFIED)))
        .build();
  }

  public Collection<AttributeType> toAttributes(CreateUserRequest request) {
    return List.of(
        toGivenNameAttribute(request.getFirstName()),
        toFamilyNameAttribute(request.getFirstName()),
        toEmailAttribute(request.getEmail()),
        toEmailVerifiedAttribute(request.isEmailVerified()));
  }

  public Collection<AttributeType> toAttributes(User user) {
    return List.of(
        toGivenNameAttribute(user.getFirstName()),
        toFamilyNameAttribute(user.getFirstName()),
        toEmailAttribute(user.getEmail()),
        toEmailVerifiedAttribute(user.isEmailVerified()));
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
