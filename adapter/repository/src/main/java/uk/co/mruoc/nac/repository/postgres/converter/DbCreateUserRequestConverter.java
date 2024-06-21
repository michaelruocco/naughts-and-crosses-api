package uk.co.mruoc.nac.repository.postgres.converter;

import java.util.Collection;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.repository.postgres.dto.DbCreateUserRequest;

public class DbCreateUserRequestConverter {

  public Collection<DbCreateUserRequest> toDbCreateUserRequests(
      Collection<CreateUserRequest> requests) {
    return requests.stream().map(this::toDbCreateUserRequest).toList();
  }

  public DbCreateUserRequest toDbCreateUserRequest(CreateUserRequest request) {
    return DbCreateUserRequest.builder()
        .username(request.getUsername())
        .firstName(request.getFirstName())
        .lastName(request.getLastName())
        .email(request.getEmail())
        .emailVerified(request.isEmailVerified())
        .build();
  }

  public Collection<CreateUserRequest> toCreateUserRequests(
      Collection<DbCreateUserRequest> dbRequests) {
    return dbRequests.stream().map(this::toCreateUserRequest).toList();
  }

  public CreateUserRequest toCreateUserRequest(DbCreateUserRequest dbRequest) {
    return CreateUserRequest.builder()
        .username(dbRequest.getUsername())
        .firstName(dbRequest.getFirstName())
        .lastName(dbRequest.getLastName())
        .email(dbRequest.getEmail())
        .emailVerified(dbRequest.isEmailVerified())
        .build();
  }
}
