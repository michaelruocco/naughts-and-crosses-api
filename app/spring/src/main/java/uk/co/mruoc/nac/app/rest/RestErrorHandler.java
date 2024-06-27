package uk.co.mruoc.nac.app.rest;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.co.mruoc.nac.usecases.UserAlreadyExistsException;
import uk.co.mruoc.nac.usecases.UserNotFoundException;

@ControllerAdvice
public class RestErrorHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<Object> handle(UserNotFoundException e, WebRequest request) {
    return doHandle(e, NOT_FOUND, request);
  }

  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<Object> handle(UserAlreadyExistsException e, WebRequest request) {
    return doHandle(e, CONFLICT, request);
  }

  private ResponseEntity<Object> doHandle(
      RuntimeException e, HttpStatus status, WebRequest request) {
    return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), status, request);
  }
}