package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import uk.co.mruoc.nac.api.dto.ApiSortOrder;
import uk.co.mruoc.nac.entities.SortOrder;

public class ApiSortConverter {

  public Collection<SortOrder> toSort(Collection<ApiSortOrder> apiSort) {
    return apiSort.stream().map(this::toSortOrder).toList();
  }

  public SortOrder toSortOrder(ApiSortOrder apiSortOrder) {
    return SortOrder.builder()
        .field(apiSortOrder.getField())
        .direction(SortOrder.Direction.valueOf(apiSortOrder.getDirection()))
        .build();
  }
}
