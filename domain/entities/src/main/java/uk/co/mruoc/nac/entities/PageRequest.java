package uk.co.mruoc.nac.entities;

import java.util.Collection;

public interface PageRequest {

  long getLimit();

  long getOffset();

  Collection<SortOrder> getSort();
}
