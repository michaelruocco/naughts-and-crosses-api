package uk.co.mruoc.nac.repository.postgres.converter;

import java.util.Collection;
import uk.co.mruoc.nac.entities.UserBatchError;
import uk.co.mruoc.nac.repository.postgres.dto.DbUserBatchError;

public class DbUserBatchErrorConverter {

  public Collection<DbUserBatchError> toDbErrors(Collection<UserBatchError> errors) {
    return errors.stream().map(this::toDbError).toList();
  }

  public DbUserBatchError toDbError(UserBatchError error) {
    return DbUserBatchError.builder()
        .username(error.getUsername())
        .message(error.getMessage())
        .build();
  }

  public Collection<UserBatchError> toErrors(Collection<DbUserBatchError> dbErrors) {
    return dbErrors.stream().map(this::toError).toList();
  }

  public UserBatchError toError(DbUserBatchError dbError) {
    return UserBatchError.builder()
        .username(dbError.getUsername())
        .message(dbError.getMessage())
        .build();
  }
}
