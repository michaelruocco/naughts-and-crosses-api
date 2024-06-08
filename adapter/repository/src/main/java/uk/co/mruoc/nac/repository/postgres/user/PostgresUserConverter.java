package uk.co.mruoc.nac.repository.postgres.user;

import java.sql.SQLException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PGobject;
import uk.co.mruoc.json.JsonConverter;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.repository.postgres.converter.DbUserConverter;
import uk.co.mruoc.nac.repository.postgres.dto.DbUser;

@RequiredArgsConstructor
@Slf4j
public class PostgresUserConverter {

  private final JsonConverter jsonConverter;
  private final DbUserConverter userConverter;

  public PostgresUserConverter(JsonConverter jsonConverter) {
    this(jsonConverter, new DbUserConverter());
  }

  public String toPostgresJson(User user) throws SQLException {
    var object = new PGobject();
    object.setType("json");
    object.setValue(toJson(user));
    return object.toString();
  }

  public User toUser(String json) {
    DbUser dbUser = jsonConverter.toObject(json, DbUser.class);
    return userConverter.toUser(dbUser);
  }

  private String toJson(User user) {
    DbUser dbUser = userConverter.toDbUser(user);
    return jsonConverter.toJson(dbUser);
  }
}
