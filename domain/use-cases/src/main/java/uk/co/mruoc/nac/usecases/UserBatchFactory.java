package uk.co.mruoc.nac.usecases;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UserBatch;

@RequiredArgsConstructor
public class UserBatchFactory {

  private final Supplier<UUID> uuidSupplier;

  public UserBatch build(Collection<CreateUserRequest> requests) {
    return UserBatch.builder().id(uuidSupplier.get().toString()).requests(requests).build();
  }
}
