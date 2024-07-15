package uk.co.mruoc.nac.entities;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PageRequest {

  private final long limit;
  private final long offset;
  private final Collection<SortOrder> sort;
}
