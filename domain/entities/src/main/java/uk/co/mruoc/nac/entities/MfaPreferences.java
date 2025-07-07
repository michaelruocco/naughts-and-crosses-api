package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class MfaPreferences {

  private final String username;
  private final MfaSettings settings;

  public MfaSetting getSoftwareTokenSetting() {
    return settings.getSoftwareToken();
  }
}
