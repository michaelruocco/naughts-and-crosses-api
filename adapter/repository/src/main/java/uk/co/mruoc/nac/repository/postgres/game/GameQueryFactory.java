package uk.co.mruoc.nac.repository.postgres.game;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.GamePageRequest;
import uk.co.mruoc.nac.entities.SortOrder;

@Slf4j
public class GameQueryFactory {

  public String toTotalQuery(GamePageRequest request) {
    String query = String.format("select count(id) from game %s", toWhereClause(request));
    log.info("built total query {}", query);
    return query;
  }

  public String toGetPageQuery(GamePageRequest request) {
    String query =
        String.format(
            "select game_json from game %s %s limit ?::int offset ?::int",
            toWhereClause(request), toSortClause(request.getSort()));
    log.info("built get page query {}", query);
    return query;
  }

  private static String toWhereClause(GamePageRequest request) {
    Optional<Boolean> complete = request.getComplete();
    if (complete.isEmpty()) {
      return "";
    }
    return "where complete = ?::boolean";
  }

  private static String toSortClause(Collection<SortOrder> sort) {
    String clauses =
        sort.stream().map(GameQueryFactory::toSortClause).collect(Collectors.joining(","));
    if (clauses.isEmpty()) {
      return "";
    }
    return String.format("order by %s", clauses);
  }

  private static String toSortClause(SortOrder order) {
    return String.format("%s %s", order.getField(), order.getDirection().name().toLowerCase());
  }
}
