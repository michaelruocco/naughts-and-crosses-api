package uk.co.mruoc.nac.repository.inmemory;

import java.util.Collection;
import java.util.Comparator;
import uk.co.mruoc.nac.entities.SortOrder;

public interface ComparatorFactory<T> {
  Comparator<T> toComparator(Collection<SortOrder> sortCriteria);
}
