package uk.co.mruoc.nac.entities;

import static uk.co.mruoc.nac.entities.SortOrder.Direction.ASC;
import static uk.co.mruoc.nac.entities.SortOrder.Direction.DESC;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
@Data
public class SortOrder {

  private final String field;
  private final Direction direction;

  public static SortOrder asc(String fieldName) {
    return new SortOrder(fieldName, ASC);
  }

  public static SortOrder desc(String fieldName) {
    return new SortOrder(fieldName, DESC);
  }

  public boolean isAscending() {
    return direction == ASC;
  }

  public enum Direction {
    ASC,
    DESC
  }
}
