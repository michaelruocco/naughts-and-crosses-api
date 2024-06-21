package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.nac.entities.UserBatch;
import uk.co.mruoc.nac.repository.postgres.converter.DbUserBatchConverter;
import uk.co.mruoc.nac.repository.postgres.dto.DbUserBatch;

@RequiredArgsConstructor
@Slf4j
public class PostgresUserBatchConverter {

  private final JsonConverter jsonConverter;
  private final DbUserBatchConverter batchConverter;

  public PostgresUserBatchConverter(JsonConverter jsonConverter) {
    this(jsonConverter, new DbUserBatchConverter());
  }

  public String toPostgresJson(UserBatch batch) throws SQLException {
    var object = new PGobject();
    object.setType("json");
    object.setValue(toJson(batch));
    return object.toString();
  }

  public UserBatch toBatch(String json) {
    DbUserBatch dbBatch = jsonConverter.toObject(json, DbUserBatch.class);
    return batchConverter.toBatch(dbBatch);
  }

  private String toJson(UserBatch batch) {
    DbUserBatch dbBatch = batchConverter.toDbBatch(batch);
    return jsonConverter.toJson(dbBatch);
  }
}
