package uk.co.mruoc.nac.api.converter;

import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiPageRequest;
import uk.co.mruoc.nac.entities.PageRequest;

@RequiredArgsConstructor
public class ApiPageRequestConverter {

  private final ApiSortByConverter sortByConverter;

  public ApiPageRequestConverter() {
    this(new ApiSortByConverter());
  }

  public PageRequest toRequest(ApiPageRequest apiRequest) {
    return PageRequest.builder()
        .limit(apiRequest.getLimit())
        .offset(apiRequest.getOffset())
        .sort(sortByConverter.toSortCriteria(apiRequest.getSortCriteria()))
        .build();
  }
}
