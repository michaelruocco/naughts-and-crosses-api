package uk.co.mruoc.nac.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserMother;

class AuthenticatedUserValidatorTest {

  private final AuthenticatedUserSupplier supplier = mock(AuthenticatedUserSupplier.class);

  private final AuthenticatedUserValidator validator = new AuthenticatedUserValidator(supplier);

  @Test
  void shouldThrowExceptionIfUserIsNotMemberOfAdminGroup() {
    User user = UserMother.user1();
    when(supplier.get()).thenReturn(user);

    Throwable error = catchThrowable(validator::validateIsAdmin);

    assertThat(error)
        .isInstanceOf(UserNotMemberOfGroupsException.class)
        .hasMessage("user %s is not a member of groups [admin]", user.getUsername());
  }

  @Test
  void shouldNotThrowExceptionIfUserIsMemberOfAdminGroup() {
    User user = UserMother.admin();
    when(supplier.get()).thenReturn(user);

    ThrowingCallable call = validator::validateIsAdmin;

    assertThatCode(call).doesNotThrowAnyException();
  }
}
