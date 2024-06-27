package uk.co.mruoc.nac.api.dto;

import java.time.Instant;
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
  private final Collection<ApiUserBatchError> errors;
  private final Instant createdAt;
  private final Instant updatedAt;
  private final boolean complete;

  public boolean isCompleteWithoutErrors() {
    System.out.println("this batch " + this);
    return complete && !hasErrors();
  }

  public boolean hasErrors() {
    return CollectionUtils.isNotEmpty(errors);
  }
}
