package uk.co.mruoc.nac.api.dto;

import java.util.Collections;
import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiUserPageRequest.ApiUserPageRequestBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiUserPageRequestMother {

  public static ApiUserPageRequest withOffset(int offset) {
    return builder().offset(offset).build();
  }

  public static ApiUserPageRequestBuilder builder() {
    return ApiUserPageRequest.builder()
        .offset(0)
        .limit(1)
        .sort(Set.of(usernameDesc()))
        .groups(Collections.emptySet());
  }

  private static ApiSortOrder usernameDesc() {
    return new ApiSortOrder("username", "DESC");
  }
}
