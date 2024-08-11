package uk.co.mruoc.nac.repository.postgres.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.GamePageRequest;
import uk.co.mruoc.nac.entities.SortOrder;

@Slf4j
public class GameQueryFactory {

  public String toTotalQuery(GamePageRequest request) {
    String query =
        String.format(
            "select count(id) from game %s %s", toJoinClause(request), toWhereClause(request));
    log.info("built total query {}", query);
    return query;
  }

  public String toGetPageQuery(GamePageRequest request) {
    String query =
        String.format(
                "select game_json from game %s %s %s limit ?::int offset ?::int",
                toJoinClause(request), toWhereClause(request), toSortClause(request.getSort()))
            .trim();
    log.info("built get page query {}", query);
    return query;
  }

  private static String toJoinClause(GamePageRequest request) {
    Optional<String> username = request.getUsername();
    if (username.isEmpty()) {
      return "";
    }
    return "left join game_player on game_id = id";
  }

  private static String toWhereClause(GamePageRequest request) {
    Collection<String> clauses = toWhereClauses(request);
    if (clauses.isEmpty()) {
      return "";
    }
    return String.format("where %s", String.join(" and ", clauses));
  }

  private static Collection<String> toWhereClauses(GamePageRequest request) {
    Collection<String> clauses = new ArrayList<>();
    request.getComplete().map(c -> "complete = ?::boolean").ifPresent(clauses::add);
    request.getUsername().map(u -> "username = ?::varchar").ifPresent(clauses::add);
    return Collections.unmodifiableCollection(clauses);
  }

  private static String toSortClause(Collection<SortOrder> sort) {
    String clauses =
        sort.stream().map(GameQueryFactory::toSortClause).collect(Collectors.joining(","));
    return String.format("order by %s", clauses);
  }

  private static String toSortClause(SortOrder order) {
    return String.format("%s %s", order.getField(), order.getDirection().name().toLowerCase());
  }
}
