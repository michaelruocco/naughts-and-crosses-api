package uk.co.mruoc.nac.repository.inmemory;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.MfaPreferences;
import uk.co.mruoc.nac.entities.MfaSettings;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.MfaSettingsRepository;
import uk.co.mruoc.nac.usecases.UserRepository;

@RequiredArgsConstructor
public class InMemoryMfaSettingsRepository implements MfaSettingsRepository {

  private final Map<String, MfaSettings> settings;
  private final UserRepository userRepository;

  public InMemoryMfaSettingsRepository(UserRepository userRepository) {
    this(new ConcurrentHashMap<>(), userRepository);
  }

  @Override
  public void save(MfaPreferences preferences) {
    String username = preferences.getUsername();
    User user = userRepository.getByUsername(username).orElseThrow();
    settings.put(username, preferences.getSettings());
    userRepository.update(user.withMfa(preferences.getSettings()));
  }

  @Override
  public Optional<MfaSettings> get(String username) {
    return Optional.ofNullable(settings.get(username));
  }
}
