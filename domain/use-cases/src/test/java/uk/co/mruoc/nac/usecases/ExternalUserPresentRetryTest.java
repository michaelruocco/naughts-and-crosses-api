package uk.co.mruoc.nac.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.Optional;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserMother;

class ExternalUserPresentRetryTest {

  private static final int MAX_ATTEMPTS = 3;
  private static final Duration WAIT_DURATION = Duration.ofMillis(5);

  private final ExternalUserService service = mock(ExternalUserService.class);

  private final ExternalUserPresentRetry retry =
      new ExternalUserPresentRetry(service, MAX_ATTEMPTS, WAIT_DURATION);

  @Test
  void shouldThrowExceptionIfUserIsNotPresentAfterMaxAttempts() {
    String username = "some-user";
    when(service.getByUsername(username)).thenReturn(Optional.empty());

    Throwable error = catchThrowable(() -> retry.waitUntilExternalUserPresent(username));

    assertThat(error)
        .isInstanceOf(UserNotFoundException.class)
        .hasMessage("user %s not found", username);
  }

  @Test
  void shouldReturnWithoutThrowingErrorAsSoonAsUserIsPresent() {
    User user = UserMother.admin();
    String username = user.getUsername();
    when(service.getByUsername(username)).thenReturn(Optional.of(user));

    ThrowingCallable call = () -> retry.waitUntilExternalUserPresent(username);

    assertThatCode(call).doesNotThrowAnyException();
  }
}
