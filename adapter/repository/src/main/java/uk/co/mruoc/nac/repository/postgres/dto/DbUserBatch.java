package uk.co.mruoc.nac.repository.postgres.dto;

import java.time.Instant;
import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Data
public class DbUserBatch {

  private final String id;
  private final Collection<DbCreateUserRequest> requests;
  private final Collection<DbUser> users;
  private final Collection<DbUserBatchError> errors;
  private final Instant createdAt;
  private final Instant updatedAt;
}
