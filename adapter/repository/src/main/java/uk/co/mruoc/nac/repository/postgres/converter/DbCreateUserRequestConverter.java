package uk.co.mruoc.nac.repository.postgres.converter;

import java.util.Collection;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.repository.postgres.dto.DbCreateUserRequest;

public class DbCreateUserRequestConverter {

  public Collection<DbCreateUserRequest> toDbCreateUserRequests(
      Collection<UpsertUserRequest> requests) {
    return requests.stream().map(this::toDbCreateUserRequest).toList();
  }

  public DbCreateUserRequest toDbCreateUserRequest(UpsertUserRequest request) {
    return DbCreateUserRequest.builder()
        .username(request.getUsername())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .emailVerified(request.isEmailVerified())
        .groups(request.getGroups())
        .build();
  }

  public Collection<UpsertUserRequest> toCreateUserRequests(
      Collection<DbCreateUserRequest> dbRequests) {
    return dbRequests.stream().map(this::toCreateUserRequest).toList();
  }

  public UpsertUserRequest toCreateUserRequest(DbCreateUserRequest dbRequest) {
    return UpsertUserRequest.builder()
        .username(dbRequest.getUsername())
        .firstName(dbRequest.getFirstName())
        .lastName(dbRequest.getLastName())
        .email(dbRequest.getEmail())
        .emailVerified(dbRequest.isEmailVerified())
        .groups(dbRequest.getGroups())
        .build();
  }
}
