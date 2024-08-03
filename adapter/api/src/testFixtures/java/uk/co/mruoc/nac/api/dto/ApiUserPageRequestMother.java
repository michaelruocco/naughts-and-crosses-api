package uk.co.mruoc.nac.api.dto;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiUserPageRequest.ApiUserPageRequestBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiUserPageRequestMother {

  public static ApiUserPageRequest withSorts(ApiSortOrder... orders) {
    return builder().sort(List.of(orders)).build();
  }

  public static ApiUserPageRequest withGroups(String... groups) {
    return builder().groups(Set.of(groups)).build();
  }

  public static ApiUserPageRequest withSearchTerm(String searchTerm) {
    return builder().searchTerm(searchTerm).build();
  }

  public static ApiUserPageRequestBuilder builder() {
    return ApiUserPageRequest.builder()
        .offset(0)
        .limit(10)
        .sort(Set.of(usernameDesc()))
        .groups(Collections.emptySet());
  }

  private static ApiSortOrder usernameDesc() {
    return new ApiSortOrder("username", "ASC");
  }
}
