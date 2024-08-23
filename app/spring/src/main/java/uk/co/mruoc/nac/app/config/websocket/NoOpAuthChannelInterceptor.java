package uk.co.mruoc.nac.app.config.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
@Slf4j
@RequiredArgsConstructor
public class NoOpAuthChannelInterceptor implements AuthChannelInterceptor {
  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    return message;
  }
}
