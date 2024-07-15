package uk.co.mruoc.nac.api.dto;

import java.util.Set;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiPageRequest.ApiPageRequestBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiPageRequestMother {

  public static ApiPageRequest withOffset(int offset) {
    return builder().offset(offset).build();
  }

  public static ApiPageRequestBuilder builder() {
    return ApiPageRequest.builder().offset(0).limit(1).sortCriteria(Set.of(usernameDesc()));
  }

  private static ApiSortOrder usernameDesc() {
    return new ApiSortOrder("username", "DESC");
  }
}
