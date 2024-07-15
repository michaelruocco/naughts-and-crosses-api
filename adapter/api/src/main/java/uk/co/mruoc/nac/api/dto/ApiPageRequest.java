package uk.co.mruoc.nac.api.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiPageRequest {

  private final long limit;
  private final long offset;
  private final Collection<ApiSortOrder> sortCriteria;
}
