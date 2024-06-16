package uk.co.mruoc.nac.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;

@RequiredArgsConstructor
@Data
public class UserBatch {

  private final String id;
  private final Collection<CreateUserRequest> requests;

  @With(AccessLevel.PRIVATE)
  private final Collection<User> users;

  @With(AccessLevel.PRIVATE)
  private final Collection<Error> errors;

  @Builder
  public UserBatch(String id, Collection<CreateUserRequest> requests) {
    this(id, requests, new ArrayList<>(), new ArrayList<>());
  }

  public int getSize() {
    return requests.size();
  }

  public boolean isComplete() {
    return getSize() == (users.size() + errors.size());
  }

  public UserBatch addUser(User user) {
    return withUsers(Stream.concat(users.stream(), Stream.of(user)).toList());
  }

  public UserBatch addError(String username, Throwable error) {
    return addError(new Error(username, error.getMessage()));
  }

  private UserBatch addError(Error error) {
    return withErrors(Stream.concat(errors.stream(), Stream.of(error)).toList());
  }

  public record Error(String username, String message) {}
}
