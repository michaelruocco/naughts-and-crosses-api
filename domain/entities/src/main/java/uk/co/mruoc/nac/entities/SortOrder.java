package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SortOrder {

  private final String field;
  private final Direction direction;

  public boolean isAscending() {
    return direction == Direction.ASC;
  }

  public enum Direction {
    ASC,
    DESC
  }
}
