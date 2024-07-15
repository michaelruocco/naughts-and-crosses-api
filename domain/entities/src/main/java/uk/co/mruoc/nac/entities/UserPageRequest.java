package uk.co.mruoc.nac.entities;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserPageRequest {

  private final Collection<String> groups;
  private final PageRequest page;

  public long getLimit() {
    return page.getLimit();
  }

  public long getOffset() {
    return page.getOffset();
  }

  public Collection<SortOrder> getSort() {
    return page.getSort();
  }
}
