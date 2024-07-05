package uk.co.mruoc.nac.app.rest;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.co.mruoc.nac.entities.IncorrectTokenForPlayerException;
import uk.co.mruoc.nac.entities.NotPlayersTurnException;
import uk.co.mruoc.nac.entities.UserNotGamePlayerException;
import uk.co.mruoc.nac.usecases.UserAlreadyExistsException;
import uk.co.mruoc.nac.usecases.UserNotAuthenticatedException;
import uk.co.mruoc.nac.usecases.UserNotFoundException;
import uk.co.mruoc.nac.usecases.UserNotMemberOfGroupsException;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserNotMemberOfGroupsException.class)
  public ResponseEntity<Object> handle(UserNotMemberOfGroupsException e, WebRequest request) {
    return doHandle(e, UNAUTHORIZED, request);
  }

  @ExceptionHandler(UserNotAuthenticatedException.class)
  public ResponseEntity<Object> handle(UserNotAuthenticatedException e, WebRequest request) {
    return doHandle(e, UNAUTHORIZED, request);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Object> handle(UserNotFoundException e, WebRequest request) {
    return doHandle(e, NOT_FOUND, request);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Object> handle(UserAlreadyExistsException e, WebRequest request) {
    return doHandle(e, CONFLICT, request);
  }

  @ExceptionHandler(NotPlayersTurnException.class)
  public ResponseEntity<Object> handle(NotPlayersTurnException e, WebRequest request) {
    return doHandle(e, BAD_REQUEST, request);
  }

  @ExceptionHandler(IncorrectTokenForPlayerException.class)
  public ResponseEntity<Object> handle(IncorrectTokenForPlayerException e, WebRequest request) {
    return doHandle(e, BAD_REQUEST, request);
  }

  @ExceptionHandler(UserNotGamePlayerException.class)
  public ResponseEntity<Object> handle(UserNotGamePlayerException e, WebRequest request) {
    return doHandle(e, BAD_REQUEST, request);
  }

  private ResponseEntity<Object> doHandle(
      RuntimeException e, HttpStatus status, WebRequest request) {
    return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), status, request);
  }
}
