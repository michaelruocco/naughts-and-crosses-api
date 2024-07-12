package uk.co.mruoc.nac.usecases;

public class JwtExpiredException extends RuntimeException {

  public JwtExpiredException(String token) {
    super(String.format("jwt expired %s", token));
  }
}
