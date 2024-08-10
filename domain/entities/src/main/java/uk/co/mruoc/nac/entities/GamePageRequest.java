package uk.co.mruoc.nac.entities;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

@Builder(toBuilder = true)
@Data
public class GamePageRequest implements PageRequest {

  private final long limit;
  private final long offset;
  private final Boolean complete;

  @Override
  public Collection<SortOrder> getSort() {
    return Set.of(SortOrder.desc("id"));
  }

  public Optional<Boolean> getComplete() {
    return Optional.ofNullable(complete);
  }
}
