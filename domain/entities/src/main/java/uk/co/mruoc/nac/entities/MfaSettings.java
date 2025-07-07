package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class MfaSettings {

  private final MfaSetting softwareToken;

  public MfaSettings() {
    this(new MfaSetting());
  }
}
