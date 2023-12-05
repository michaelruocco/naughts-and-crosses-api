package uk.co.mruoc.nac.app.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.WebSocketHttpHeaders;

import java.util.Objects;

@Slf4j
public class AuthChannelInterceptor implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (isConnectMessage(accessor)) {
            log.info("handling connect message");
            log.info("auth header {}", accessor.getNativeHeader(HttpHeaders.AUTHORIZATION));
            //Authentication user = ... ; // a
            //accessor.setUser(user);
        }
        return message;
    }

    private boolean isConnectMessage(StompHeaderAccessor accessor) {
        return StompCommand.CONNECT.equals(accessor.getCommand());
    }
}
