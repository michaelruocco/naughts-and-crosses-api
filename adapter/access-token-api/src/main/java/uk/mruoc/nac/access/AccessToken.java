package uk.mruoc.nac.access;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Builder
@Data
@Slf4j
public class AccessToken {

  private final String type;
  private final String value;
  private final Instant expiry;

  public String getTypeAndValue() {
    return String.format("%s %s", type, value);
  }

  public boolean isExpired(Instant now) {
    boolean expired = now.isAfter(expiry);
    log.debug("token expired {} expiry {} cutoff {}", expired, expiry, now);
    return expired;
  }
}
