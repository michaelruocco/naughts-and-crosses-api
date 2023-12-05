package uk.mruoc.nac.access;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
public class CacheAccessTokenClientDecorator implements AccessTokenClient {

    private final AccessTokenClient client;
    private final Clock clock;
    private final Duration cutoffBuffer;

    private AccessToken token;

    public CacheAccessTokenClientDecorator(AccessTokenClient client) {
        this(client, Clock.systemUTC(), Duration.ofSeconds(30));
    }

    @Override
    public AccessToken generateToken() {
        if (isNewTokenRequired()) {
            token = client.generateToken();
        }
        return token;
    }

    private boolean isNewTokenRequired() {
        Instant cutoff = clock.instant().plus(cutoffBuffer);
        boolean required = token == null || token.isExpired(cutoff);
        log.debug("new token required {}", required);
        return required;
    }
}
