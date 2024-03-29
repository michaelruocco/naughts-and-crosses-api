package uk.co.mruoc.nac.app.config.security;

import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.nac.app.config.websocket.AuthChannelInterceptor;
import uk.co.mruoc.nac.app.config.websocket.NoOpAuthChannelInterceptor;
import uk.co.mruoc.nac.usecases.UserProvider;
import uk.co.mruoc.nac.user.inmemory.StubUserProvider;

@ConditionalOnProperty(value = "auth.security.enabled", havingValue = "false")
@Configuration
@EnableAutoConfiguration(
    exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
public class DisableSecurityAutoConfig {

  @Bean
  public AuthChannelInterceptor noOpAuthChannelInterceptor() {
    return new NoOpAuthChannelInterceptor();
  }

  @Bean
  public UserProvider stubUserProvider() {
    return new StubUserProvider();
  }
}
