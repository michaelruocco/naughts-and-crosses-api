package uk.co.mruoc.nac.entities;

import java.time.Instant;
import java.util.Collection;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;

@RequiredArgsConstructor
@Builder(toBuilder = true)
@Data
public class UserBatch {

  private final String id;
  private final Collection<UpsertUserRequest> requests;

  @With(AccessLevel.PRIVATE)
  private final Collection<User> users;

  @With(AccessLevel.PRIVATE)
  private final Collection<UserBatchError> errors;

  private final Instant createdAt;
  private final Instant updatedAt;

  public int getSize() {
    return requests.size();
  }

  public boolean isComplete() {
    return getSize() == (users.size() + errors.size());
  }

  public UserBatch addUser(User user, Instant now) {
    return toBuilder()
        .users(Stream.concat(users.stream(), Stream.of(user)).toList())
        .updatedAt(now)
        .build();
  }

  public UserBatch addError(UserBatchError error, Instant now) {
    return toBuilder()
        .errors(Stream.concat(errors.stream(), Stream.of(error)).toList())
        .updatedAt(now)
        .build();
  }
}
