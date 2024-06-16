package uk.co.mruoc.nac.api.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiUserBatch {

  private final String id;
  private final Collection<ApiCreateUserRequest> requests;
  private final Collection<ApiUser> users;
  private final Collection<Error> errors;
  private final boolean complete;

  public boolean hasErrors() {
    return CollectionUtils.isNotEmpty(errors);
  }

  @RequiredArgsConstructor
  @NoArgsConstructor(force = true)
  @Builder
  @Data
  public static class Error {
    private final String username;
    private final String message;
  }
}
