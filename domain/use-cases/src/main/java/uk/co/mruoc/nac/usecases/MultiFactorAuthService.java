package uk.co.mruoc.nac.usecases;

import lombok.Builder;
import uk.co.mruoc.nac.entities.MfaPreferences;
import uk.co.mruoc.nac.entities.MfaSettings;
import uk.co.mruoc.nac.entities.VerifySoftwareTokenRequest;

@Builder
public class MultiFactorAuthService {

  private final JwtTokenSupplier tokenSupplier;
  private final SoftwareTokenClient softwareTokenClient;
  private final AuthenticatedUserValidator userValidator;
  private final MfaSettingsRepository repository;

  public String createSoftwareToken() {
    return softwareTokenClient.create(tokenSupplier.get());
  }

  public void verifySoftwareToken(VerifySoftwareTokenRequest request) {
    softwareTokenClient.verify(request.withAccessToken(tokenSupplier.get()));
  }

  public void updatePreferences(MfaPreferences preferences) {
    userValidator.validateIsAdminOrUser(preferences.getUsername());
    softwareTokenClient.update(preferences);
    repository.save(preferences);
  }

  public MfaSettings getSettings(String username) {
    return repository.get(username).orElse(new MfaSettings());
  }
}
