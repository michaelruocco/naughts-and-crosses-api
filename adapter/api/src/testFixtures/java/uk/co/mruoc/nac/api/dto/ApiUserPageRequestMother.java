package uk.co.mruoc.nac.api.dto;

import java.util.Collections;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiUserPageRequest.ApiUserPageRequestBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiUserPageRequestMother {

  public static ApiUserPageRequest withPage(ApiPageRequest page) {
    return builder().page(page).build();
  }

  public static ApiUserPageRequestBuilder builder() {
    return ApiUserPageRequest.builder().groups(Collections.emptySet());
  }
}
