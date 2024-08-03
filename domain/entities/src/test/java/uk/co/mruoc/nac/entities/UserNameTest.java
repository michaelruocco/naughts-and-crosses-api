package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class UserNameTest {

  @Test
  void shouldPopulateFullNameFromFirstAndLastName() {
    UserName name = UserName.builder().first("Dave").last("Roberts").build();

    UserName populatedName = name.tryToPopulateAll();

    assertThat(populatedName.getFull()).isEqualTo("Dave Roberts");
    assertThat(populatedName.getFirst()).isEqualTo("Dave");
    assertThat(populatedName.getLast()).isEqualTo("Roberts");
  }

  @Test
  void shouldPopulateFirstAndLastNameFromFullName() {
    UserName name = UserName.builder().full("Dave Roberts").build();

    UserName populatedName = name.tryToPopulateAll();

    assertThat(populatedName.getFull()).isEqualTo("Dave Roberts");
    assertThat(populatedName.getFirst()).isEqualTo("Dave");
    assertThat(populatedName.getLast()).isEqualTo("Roberts");
  }

  @Test
  void shouldAttemptToHandleNameWithMoreThanOneSpace() {
    UserName name = UserName.builder().full("Mr Dave Roberts").build();

    UserName populatedName = name.tryToPopulateAll();

    assertThat(populatedName.getFull()).isEqualTo("Mr Dave Roberts");
    assertThat(populatedName.getFirst()).isEqualTo("Mr");
    assertThat(populatedName.getLast()).isEqualTo("Dave Roberts");
  }

  @Test
  void shouldAttemptToHandleSingleName() {
    UserName name = UserName.builder().full("Roberts").build();

    UserName populatedName = name.tryToPopulateAll();

    assertThat(populatedName.getFull()).isEqualTo("Roberts");
    assertThat(populatedName.getFirst()).isNull();
    assertThat(populatedName.getLast()).isEqualTo("Roberts");
  }

  @Test
  void shouldHandleJustFirstName() {
    UserName name = UserName.builder().first("Dave").build();

    UserName populatedName = name.tryToPopulateAll();

    assertThat(populatedName.getFull()).isEqualTo("Dave");
    assertThat(populatedName.getFirst()).isEqualTo("Dave");
    assertThat(populatedName.getLast()).isNull();
  }

  @Test
  void shouldHandleJustLastName() {
    UserName name = UserName.builder().last("Roberts").build();

    UserName populatedName = name.tryToPopulateAll();

    assertThat(populatedName.getFull()).isEqualTo("Roberts");
    assertThat(populatedName.getFirst()).isNull();
    assertThat(populatedName.getLast()).isEqualTo("Roberts");
  }

  @Test
  void shouldDoNothingIfNoNamesPopulated() {
    UserName name = UserName.builder().build();

    UserName populatedName = name.tryToPopulateAll();

    assertThat(populatedName.getFull()).isNull();
    assertThat(populatedName.getFirst()).isNull();
    assertThat(populatedName.getLast()).isNull();
  }
}
