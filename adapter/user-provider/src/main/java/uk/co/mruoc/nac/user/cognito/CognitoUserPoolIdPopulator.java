package uk.co.mruoc.nac.user.cognito;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CognitoUserPoolIdPopulator {

  private static final String DEFAULT_PLACE_HOLDER = "%POOL_ID%";

  private final String poolId;
  private final String placeholder;

  public CognitoUserPoolIdPopulator(String poolId) {
    this(poolId, DEFAULT_PLACE_HOLDER);
  }

  public String replacePoolIdIfRequired(String initial) {
    if (!initial.contains(placeholder)) {
      log.info(
          "initial {} does not contain placeholder {} so no replacement required",
          initial,
          placeholder);
      return initial;
    }
    String replaced = initial.replace(placeholder, poolId);
    log.info("replaced initial value {} with {}", initial, replaced);
    return replaced;
  }
}
