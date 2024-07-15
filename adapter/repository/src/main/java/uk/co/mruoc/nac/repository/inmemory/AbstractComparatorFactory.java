package uk.co.mruoc.nac.repository.inmemory;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.SortNotSupportedException;
import uk.co.mruoc.nac.entities.SortOrder;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractComparatorFactory<T> implements ComparatorFactory<T> {

  private final Map<String, Comparator<T>> fieldComparators;
  private final Comparator<T> defaultComparator;

  @Override
  public Comparator<T> toComparator(Collection<SortOrder> sortCriteria) {
    return sortCriteria.stream()
        .map(this::toComparator)
        .reduce(Comparator::thenComparing)
        .orElse(defaultComparator);
  }

  private Comparator<T> toComparator(SortOrder sortOrder) {
    Comparator<T> comparator = toComparator(sortOrder.getField());
    if (sortOrder.isAscending()) {
      return comparator;
    }
    return comparator.reversed();
  }

  private Comparator<T> toComparator(String fieldName) {
    return Optional.ofNullable(fieldComparators.get(fieldName))
        .orElseThrow(() -> new SortNotSupportedException(fieldName));
  }
}
