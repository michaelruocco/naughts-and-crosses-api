package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Builder(toBuilder = true)
@Data
public class UserName {

  private final String full;
  private final String first;
  private final String last;

  public UserName tryToPopulateAll() {
    return UserNameUtils.tryPopulateAll(this);
  }

  public boolean hasFirstAndLastName() {
    return hasFirstName() && hasLastName();
  }

  public boolean hasFirstName() {
    return StringUtils.isNotEmpty(first);
  }

  public boolean hasLastName() {
    return StringUtils.isNotEmpty(last);
  }

  public boolean hasFullName() {
    return StringUtils.isNotEmpty(full);
  }
}
