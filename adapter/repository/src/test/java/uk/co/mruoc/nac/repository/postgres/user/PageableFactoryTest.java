package uk.co.mruoc.nac.repository.postgres.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import uk.co.mruoc.nac.entities.PageRequest;
import uk.co.mruoc.nac.entities.UserPageRequestMother;

class PageableFactoryTest {

  private final PageableFactory factory = new PageableFactory();

  @Test
  void shouldPopulatePageSizeWithLimit() {
    PageRequest request = UserPageRequestMother.build();

    Pageable pageable = factory.build(request);

    assertThat(pageable.getPageSize()).isEqualTo(request.getLimit());
  }

  @Test
  void shouldPopulateOffset() {
    PageRequest request = UserPageRequestMother.build();

    Pageable pageable = factory.build(request);

    assertThat(pageable.getOffset()).isEqualTo(request.getOffset());
  }

  @Test
  void shouldPopulateSort() {
    PageRequest request = UserPageRequestMother.build();

    Pageable pageable = factory.build(request);

    assertThat(pageable.getSort()).containsExactly(Sort.Order.asc("username"));
  }
}
