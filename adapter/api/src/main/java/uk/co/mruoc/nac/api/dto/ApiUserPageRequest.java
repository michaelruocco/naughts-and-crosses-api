package uk.co.mruoc.nac.api.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiUserPageRequest {

  private final long limit;
  private final long offset;
  private final Collection<ApiSortOrder> sort;
  private final Collection<String> groups;

  public Collection<ApiSortOrder> getSort() {
    return emptyIfNull(sort);
  }

  public Collection<String> getGroups() {
    return emptyIfNull(groups);
  }

  private <T> Collection<T> emptyIfNull(Collection<T> values) {
    return Optional.ofNullable(values).orElse(Collections.emptySet());
  }
}
