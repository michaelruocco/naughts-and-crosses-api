package uk.co.mruoc.nac.app.config.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.nac.app.config.websocket.AuthChannelInterceptor;
import uk.co.mruoc.nac.app.config.websocket.NoOpAuthChannelInterceptor;
@Configuration
@EnableAutoConfiguration(exclude = { SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class })
@ConditionalOnProperty(name = "auth.security.enabled", havingValue = "false")
public class DisabledSecurityConfig {
  @Bean
  public AuthChannelInterceptor authChannelInterceptor() {
    return new NoOpAuthChannelInterceptor();
  }
}
