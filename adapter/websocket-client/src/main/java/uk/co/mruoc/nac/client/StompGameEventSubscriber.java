package uk.co.mruoc.nac.client;

import org.springframework.messaging.simp.stomp.StompFrameHandler;

public interface StompGameEventSubscriber<T> extends GameEventSubscriber<T>, StompFrameHandler {
  // intentionally blank
}
