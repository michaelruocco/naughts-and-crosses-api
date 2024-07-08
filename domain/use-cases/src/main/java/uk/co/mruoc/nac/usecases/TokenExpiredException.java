package uk.co.mruoc.nac.usecases;

public class TokenExpiredException extends RuntimeException {

  public TokenExpiredException(String token) {
    super(String.format("token expired %s", token));
  }
}
