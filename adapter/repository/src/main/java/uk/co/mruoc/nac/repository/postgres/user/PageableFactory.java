package uk.co.mruoc.nac.repository.postgres.user;

import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.co.mruoc.nac.entities.PageRequest;
import uk.co.mruoc.nac.entities.SortOrder;

public class PageableFactory {

  public Pageable build(PageRequest request) {
    return OffsetBasedPageable.builder()
        .limit(request.getLimit())
        .offset(request.getOffset())
        .sort(toDbSort(request.getSort()))
        .build();
  }

  private static Sort toDbSort(Collection<SortOrder> orders) {
    return Sort.by(toDbOrders(orders));
  }

  private static List<Sort.Order> toDbOrders(Collection<SortOrder> sortOrders) {
    return sortOrders.stream().map(PageableFactory::toDbOrder).toList();
  }

  private static Sort.Order toDbOrder(SortOrder sortOrder) {
    String field = sortOrder.getField();
    if (sortOrder.isAscending()) {
      return Sort.Order.asc(field);
    }
    return Sort.Order.desc(field);
  }
}
