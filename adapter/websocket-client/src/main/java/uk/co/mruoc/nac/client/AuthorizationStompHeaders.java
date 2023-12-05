package uk.co.mruoc.nac.client;

import org.springframework.messaging.simp.stomp.StompHeaders;

public class AuthorizationStompHeaders extends StompHeaders {

  public AuthorizationStompHeaders(String token) {
    this.set("Authorization", token);
  }
}
