package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import uk.co.mruoc.nac.entities.MfaPreferences;
import uk.co.mruoc.nac.entities.MfaSettings;

public interface MfaSettingsRepository {

  void save(MfaPreferences preferences);

  Optional<MfaSettings> get(String username);
}
