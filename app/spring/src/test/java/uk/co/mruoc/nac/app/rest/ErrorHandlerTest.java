package uk.co.mruoc.nac.app.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import uk.co.mruoc.nac.entities.IncorrectTokenForPlayerException;
import uk.co.mruoc.nac.entities.NotPlayersTurnException;
import uk.co.mruoc.nac.entities.UserNotGamePlayerException;
import uk.co.mruoc.nac.usecases.UserAlreadyExistsException;
import uk.co.mruoc.nac.usecases.UserNotAuthenticatedException;
import uk.co.mruoc.nac.usecases.UserNotFoundException;
import uk.co.mruoc.nac.usecases.UserNotMemberOfGroupsException;

class ErrorHandlerTest {

  private static final String USERNAME = "some-username";

  private final ErrorHandler handler = new ErrorHandler();

  @Test
  void shouldReturnUnauthorizedStatusForUserNotMemberOfGroupsException() {
    UserNotMemberOfGroupsException error =
        new UserNotMemberOfGroupsException(USERNAME, "test-groups");
    WebRequest webRequest = mock(WebRequest.class);

    ResponseEntity<Object> response = handler.handle(error, webRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(response.getBody()).isEqualTo(error.getMessage());
    assertThat(response.getHeaders()).isEmpty();
  }

  @Test
  void shouldReturnUnauthorizedStatusForUserNotAuthenticatedException() {
    UserNotAuthenticatedException error = new UserNotAuthenticatedException();
    WebRequest webRequest = mock(WebRequest.class);

    ResponseEntity<Object> response = handler.handle(error, webRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    assertThat(response.getBody()).isEqualTo(error.getMessage());
    assertThat(response.getHeaders()).isEmpty();
  }

  @Test
  void shouldReturnNotFoundStatusForUserNotFoundException() {
    UserNotFoundException error = new UserNotFoundException(USERNAME);
    WebRequest webRequest = mock(WebRequest.class);

    ResponseEntity<Object> response = handler.handle(error, webRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody()).isEqualTo(error.getMessage());
    assertThat(response.getHeaders()).isEmpty();
  }

  @Test
  void shouldReturnConflictStatusForUserAlreadyExistsException() {
    UserAlreadyExistsException error = new UserAlreadyExistsException(USERNAME);
    WebRequest webRequest = mock(WebRequest.class);

    ResponseEntity<Object> response = handler.handle(error, webRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody()).isEqualTo(error.getMessage());
    assertThat(response.getHeaders()).isEmpty();
  }

  @Test
  void shouldReturnBadRequestStatusForNotPlayersTurnException() {
    NotPlayersTurnException error = new NotPlayersTurnException(USERNAME);
    WebRequest webRequest = mock(WebRequest.class);

    ResponseEntity<Object> response = handler.handle(error, webRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(error.getMessage());
    assertThat(response.getHeaders()).isEmpty();
  }

  @Test
  void shouldReturnBadRequestStatusForIncorrectTokenForPlayerException() {
    IncorrectTokenForPlayerException error = new IncorrectTokenForPlayerException('X', USERNAME);
    WebRequest webRequest = mock(WebRequest.class);

    ResponseEntity<Object> response = handler.handle(error, webRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(error.getMessage());
    assertThat(response.getHeaders()).isEmpty();
  }

  @Test
  void shouldReturnBadRequestStatusForUserNotGamePlayerException() {
    UserNotGamePlayerException error = new UserNotGamePlayerException(USERNAME, 1L);
    WebRequest webRequest = mock(WebRequest.class);

    ResponseEntity<Object> response = handler.handle(error, webRequest);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody()).isEqualTo(error.getMessage());
    assertThat(response.getHeaders()).isEmpty();
  }
}
