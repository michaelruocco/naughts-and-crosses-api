package uk.co.mruoc.nac.entities;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

@Builder(toBuilder = true)
@Data
public class UserPageRequest implements PageRequest {

  private final long limit;
  private final long offset;
  private final Collection<SortOrder> sort;
  private final Collection<String> groups;

  @Override
  public Collection<SortOrder> getSort() {
    return defaultIfEmpty(sort, Set.of(SortOrder.asc("username")));
  }

  public Collection<String> getGroups() {
    return defaultIfEmpty(groups, Collections.emptySet());
  }

  private <T> Collection<T> defaultIfEmpty(Collection<T> values, Collection<T> defaults) {
    if (CollectionUtils.isEmpty(values)) {
      return defaults;
    }
    return values;
  }
}
