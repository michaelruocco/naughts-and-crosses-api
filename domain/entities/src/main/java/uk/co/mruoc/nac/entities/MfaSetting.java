package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class MfaSetting {

  private final boolean enabled;
  private final boolean preferred;

  public MfaSetting() {
    this(false, false);
  }
}
