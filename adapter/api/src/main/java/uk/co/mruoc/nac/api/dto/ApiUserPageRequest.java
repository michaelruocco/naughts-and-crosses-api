package uk.co.mruoc.nac.api.dto;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiUserPageRequest {

  private final Collection<String> groups;
  private final ApiPageRequest page;

  public Collection<String> getGroups() {
    return Optional.ofNullable(groups).orElse(Collections.emptySet());
  }

  public ApiPageRequest getPage() {
    return Optional.ofNullable(page).orElse(defaultPage());
  }

  private static ApiPageRequest defaultPage() {
    return ApiPageRequest.builder()
        .limit(25)
        .offset(0)
        .sortCriteria(Set.of(new ApiSortOrder("username", "ASC")))
        .build();
  }
}
