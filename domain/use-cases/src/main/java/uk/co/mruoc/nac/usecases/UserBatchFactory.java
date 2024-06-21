package uk.co.mruoc.nac.usecases;

import java.time.Clock;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.Builder;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UserBatch;

@Builder
public class UserBatchFactory {

  private final Clock clock;
  private final Supplier<UUID> uuidSupplier;

  public UserBatch build(Collection<CreateUserRequest> requests) {
    Instant now = clock.instant();
    return UserBatch.builder()
        .id(uuidSupplier.get().toString())
        .requests(requests)
        .users(Collections.emptyList())
        .errors(Collections.emptyList())
        .createdAt(now)
        .updatedAt(now)
        .build();
  }
}
