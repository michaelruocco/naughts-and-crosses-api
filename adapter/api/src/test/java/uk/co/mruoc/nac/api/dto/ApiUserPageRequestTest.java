package uk.co.mruoc.nac.api.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import org.junit.jupiter.api.Test;

class ApiUserPageRequestTest {

  @Test
  void shouldReturnEmptySortIfNull() {
    ApiUserPageRequest request = ApiUserPageRequest.builder().sort(null).build();

    Collection<ApiSortOrder> sort = request.getSort();

    assertThat(sort).isNotNull().isEmpty();
  }

  @Test
  void shouldReturnEmptyGroupsIfNull() {
    ApiUserPageRequest request = ApiUserPageRequest.builder().groups(null).build();

    Collection<String> groups = request.getGroups();

    assertThat(groups).isNotNull().isEmpty();
  }
}
