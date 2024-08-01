package uk.co.mruoc.nac.user.inmemory;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StubUserTokenConfigs {

  private final Collection<StubUserTokenConfig> configs;

  public StubUserTokenConfigs() {
    this(buildAll());
  }

  public Optional<StubUserTokenConfig> getByUsername(String username) {
    return getBy(config -> config.hasUsername(username));
  }

  public Optional<StubUserTokenConfig> getByAuthCode(String authCode) {
    return getBy(config -> config.hasAuthCode(authCode));
  }

  private Optional<StubUserTokenConfig> getBy(Predicate<StubUserTokenConfig> predicate) {
    return configs.stream().filter(predicate).findFirst();
  }

  private static Collection<StubUserTokenConfig> buildAll() {
    return List.of(admin(), user1(), user2());
  }

  private static StubUserTokenConfig admin() {
    return StubUserTokenConfig.builder()
        .username("admin")
        .password("pwd")
        .subject("42bfdad9-c66a-4d5a-aa48-a97d6d3574af")
        .authCode("1b50c4fa-1f74-44f9-800a-995793dc2595")
        .build();
  }

  private static StubUserTokenConfig user1() {
    return StubUserTokenConfig.builder()
        .username("user-1")
        .password("pwd1")
        .subject("707d9fa6-13dd-4985-93aa-a28f01e89a6b")
        .authCode("987c4041-a5ed-4509-a12a-b34453f77441")
        .build();
  }

  private static StubUserTokenConfig user2() {
    return StubUserTokenConfig.builder()
        .username("user-2")
        .password("pwd2")
        .subject("dadfde25-9924-4982-802d-dfd0bce2218d")
        .authCode("d5be6ed3-d92f-4e7a-94e2-8ada31a66f2d")
        .build();
  }
}
