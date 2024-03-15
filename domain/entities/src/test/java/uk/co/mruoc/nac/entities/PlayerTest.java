package uk.co.mruoc.nac.entities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class PlayerTest {

  @Test
  void shouldReturnTrueIfHasToken() {
    char token = 'X';
    Player player = Player.builder().token(token).build();

    boolean result = player.hasToken(token);

    assertThat(result).isTrue();
  }

  @Test
  void shouldReturnFalseIfDoesNotHaveToken() {
    Player player = Player.builder().token('X').build();

    boolean result = player.hasToken('Y');

    assertThat(result).isFalse();
  }
}
