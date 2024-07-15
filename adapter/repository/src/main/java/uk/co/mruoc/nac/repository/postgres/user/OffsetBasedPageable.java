package uk.co.mruoc.nac.repository.postgres.user;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@RequiredArgsConstructor
@Data
public class OffsetBasedPageable implements Pageable {

  private final long limit;
  @With private final long offset;
  private final Sort sort;

  @Override
  public int getPageNumber() {
    return Math.toIntExact(offset / limit);
  }

  @Override
  public int getPageSize() {
    return Math.toIntExact(limit);
  }

  @Override
  public long getOffset() {
    return Math.toIntExact(offset);
  }

  @Override
  public Sort getSort() {
    return sort;
  }

  @Override
  public Pageable next() {
    long newOffset = getOffset() + getPageSize();
    return withOffset(newOffset);
  }

  @Override
  public Pageable previousOrFirst() {
    if (hasPrevious()) {
      long newOffset = offset - limit;
      return withOffset(newOffset);
    }
    return first();
  }

  @Override
  public Pageable first() {
    return withOffset(0);
  }

  @Override
  public Pageable withPage(int pageNumber) {
    long newOffset = pageNumber * limit;
    return withOffset(newOffset);
  }

  @Override
  public boolean hasPrevious() {
    return offset > limit;
  }
}
