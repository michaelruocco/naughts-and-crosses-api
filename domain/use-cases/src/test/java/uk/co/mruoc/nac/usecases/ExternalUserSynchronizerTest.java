package uk.co.mruoc.nac.usecases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserMother;

class ExternalUserSynchronizerTest {

  private final AuthenticatedUserValidator userValidator = mock(AuthenticatedUserValidator.class);
  private final ExternalUserService externalUserService = mock(ExternalUserService.class);
  private final UserRepository repository = mock(UserRepository.class);

  private final ExternalUserSynchronizer synchronizer =
      ExternalUserSynchronizer.builder()
          .userValidator(userValidator)
          .externalUserService(externalUserService)
          .repository(repository)
          .build();

  @Test
  void shouldSynchronizeIfUserDoesNotExistInRepository() {
    User user = UserMother.admin();
    String username = user.getUsername();
    when(repository.exists(username)).thenReturn(false);
    when(externalUserService.getByUsername(username)).thenReturn(Optional.of(user));

    synchronizer.synchronizeIfNotPresent(username);

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    verify(repository).upsert(captor.capture());
    assertThat(captor.getValue()).isEqualTo(user);
  }

  @Test
  void shouldNotSynchronizeIfUserDoesExistInRepository() {
    User user = UserMother.admin();
    String username = user.getUsername();
    when(repository.exists(username)).thenReturn(true);

    synchronizer.synchronizeIfNotPresent(username);

    verifyNoInteractions(externalUserService);
    verify(repository, never()).update(any(User.class));
    verify(repository, never()).delete(anyString());
  }

  @Test
  void synchronizeShouldDeleteUserIfPresentInRepositoryAndNotInCognito() {
    User user = UserMother.admin();
    when(repository.getAll()).thenReturn(Stream.of(user));
    when(externalUserService.getAllUsers()).thenReturn(Stream.empty());
    when(externalUserService.getByUsername(user.getUsername())).thenReturn(Optional.empty());

    synchronizer.synchronize();

    ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
    verify(repository).delete(captor.capture());
    assertThat(captor.getValue()).isEqualTo(user.getUsername());
  }

  @Test
  void synchronizeShouldNotDeleteUserIfPresentInRepositoryAndInCognito() {
    User user = UserMother.admin();
    when(repository.getAll()).thenReturn(Stream.of(user));
    when(externalUserService.getAllUsers()).thenReturn(Stream.of(user));
    when(externalUserService.getByUsername(user.getUsername())).thenReturn(Optional.of(user));

    synchronizer.synchronize();

    verify(repository, never()).delete(anyString());
  }
}
