package uk.co.mruoc.nac.user.keycloak;

import java.util.Collection;
import java.util.stream.Stream;
import org.keycloak.representations.idm.UserRepresentation;
import uk.co.mruoc.nac.entities.User;

public class KeycloakUserConverter {

  public Stream<User> toUsers(Collection<UserRepresentation> users) {
    return users.stream().map(this::toUser);
  }

  public User toUser(UserRepresentation user) {
    return User.builder()
        .id(user.getId())
        .username(user.getUsername())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .build();
  }
}
