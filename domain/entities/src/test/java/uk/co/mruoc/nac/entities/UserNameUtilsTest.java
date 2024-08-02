package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserNameUtilsTest {

  @Test
  void shouldHandleNullFullName() {
    String full = null;

    UserName name = UserNameUtils.toUserName(full);

    assertThat(name.getFull()).isNull();
    assertThat(name.getFirst()).isNull();
    assertThat(name.getLast()).isNull();
  }
}
