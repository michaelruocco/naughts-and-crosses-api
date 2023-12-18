package uk.co.mruoc.nac.user.keycloak;

import org.keycloak.representations.idm.UserRepresentation;
import uk.co.mruoc.nac.entities.User;

import java.util.Collection;
import java.util.stream.Stream;

public class KeycloakUserConverter {

    public Stream<User> toUsers(Collection<UserRepresentation> users) {
        return users.stream().map(this::toUser);
    }

    public User toUser(UserRepresentation user) {
        return User.builder()
                .id(user.getId())
                .name(toName(user))
                .email(user.getEmail())
                .build();
    }
    private static String toName(UserRepresentation user) {
        return String.format("%s %s", user.getFirstName(), user.getLastName());
    }
}