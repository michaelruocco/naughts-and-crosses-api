package uk.co.mruoc.nac.api.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiUserBatch {

  private final String id;
  private final int size;
  private final Collection<ApiUser> users;
  private final Collection<Error> errors;

  @RequiredArgsConstructor
  @NoArgsConstructor(force = true)
  @Builder
  @Data
  public static class Error {
    private final String username;
    private final String message;
  }
}
