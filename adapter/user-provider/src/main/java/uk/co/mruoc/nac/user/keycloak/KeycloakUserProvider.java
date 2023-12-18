package uk.co.mruoc.nac.user.keycloak;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.UserRepresentation;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.UserProvider;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class KeycloakUserProvider implements UserProvider {

    private final Keycloak adminClient;
    private final String realm;
    private final KeycloakUserConverter converter;

    public KeycloakUserProvider(KeycloakAdminConfig config) {
        this(buildAdminClient(config), config.getRealm(), new KeycloakUserConverter());
    }

    private static Keycloak buildAdminClient(KeycloakAdminConfig config) {
        return KeycloakBuilder.builder()
                .serverUrl(config.getUrl())
                .realm(config.getRealm())
                .grantType(config.getGrantType())
                .clientId(config.getClientId())
                .clientSecret(config.getClientSecret())
                .build();
    }

    @Override
    public Optional<User> get(String id) {
        UserRepresentation user = adminClient.realm(realm).users().get(id).toRepresentation();
        return Optional.of(converter.toUser(user));
    }

    @Override
    public Stream<User> getAll() {
        List<UserRepresentation> users = adminClient.realm(realm).users().list();
        return converter.toUsers(users);
    }
}
