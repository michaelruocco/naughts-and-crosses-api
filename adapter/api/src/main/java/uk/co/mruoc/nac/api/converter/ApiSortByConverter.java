package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import uk.co.mruoc.nac.api.dto.ApiSortOrder;
import uk.co.mruoc.nac.entities.SortOrder;

public class ApiSortByConverter {

  public Collection<SortOrder> toSortCriteria(Collection<ApiSortOrder> apiCriteria) {
    return apiCriteria.stream().map(this::toSortBy).toList();
  }

  public SortOrder toSortBy(ApiSortOrder sortBy) {
    return SortOrder.builder()
        .field(sortBy.getField())
        .direction(SortOrder.Direction.valueOf(sortBy.getDirection()))
        .build();
  }
}
