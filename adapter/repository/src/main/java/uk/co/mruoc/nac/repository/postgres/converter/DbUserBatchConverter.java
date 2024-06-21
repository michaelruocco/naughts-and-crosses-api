package uk.co.mruoc.nac.repository.postgres.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.UserBatch;
import uk.co.mruoc.nac.repository.postgres.dto.DbUserBatch;

@RequiredArgsConstructor
public class DbUserBatchConverter {

  private final DbCreateUserRequestConverter requestConverter;
  private final DbUserConverter userConverter;
  private final DbUserBatchErrorConverter errorConverter;

  public DbUserBatchConverter() {
    this(
        new DbCreateUserRequestConverter(), new DbUserConverter(), new DbUserBatchErrorConverter());
  }

  public DbUserBatch toDbBatch(UserBatch batch) {
    return DbUserBatch.builder()
        .id(batch.getId())
        .requests(requestConverter.toDbCreateUserRequests(batch.getRequests()))
        .users(userConverter.toDbUsers(batch.getUsers()))
        .errors(errorConverter.toDbErrors(batch.getErrors()))
        .createdAt(batch.getCreatedAt())
        .updatedAt(batch.getUpdatedAt())
        .build();
  }

  public UserBatch toBatch(DbUserBatch dbBatch) {
    return UserBatch.builder()
        .id(dbBatch.getId())
        .requests(requestConverter.toCreateUserRequests(dbBatch.getRequests()))
        .users(userConverter.toUsers(dbBatch.getUsers()))
        .errors(errorConverter.toErrors(dbBatch.getErrors()))
        .createdAt(dbBatch.getCreatedAt())
        .updatedAt(dbBatch.getUpdatedAt())
        .build();
  }
}
