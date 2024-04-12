package uk.co.mruoc.nac.app.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.co.mruoc.nac.api.converter.ApiConverter;
import uk.co.mruoc.nac.api.converter.ApiUserConverter;
import uk.co.mruoc.nac.app.config.security.CorsWebMvcConfigurer;
import uk.co.mruoc.nac.app.config.websocket.BrokerConfig;
import uk.co.mruoc.nac.usecases.BoardFormatter;
import uk.co.mruoc.nac.usecases.GameEventPublisher;
import uk.co.mruoc.nac.usecases.GameFacade;
import uk.co.mruoc.nac.usecases.GameFactory;
import uk.co.mruoc.nac.usecases.GameRepository;
import uk.co.mruoc.nac.usecases.GameService;
import uk.co.mruoc.nac.usecases.IdSupplier;
import uk.co.mruoc.nac.usecases.UserProvider;
import uk.co.mruoc.nac.usecases.UserService;

@Configuration
public class ApplicationConfig {

  @Bean
  public GameFacade gameFacade(UserService userService, GameService gameService) {
    return GameFacade.builder().userService(userService).gameService(gameService).build();
  }

  @Bean
  public UserService userService(UserProvider provider) {
    return new UserService(provider);
  }

  @Bean
  public GameFactory gameFactory(IdSupplier idSupplier) {
    return new GameFactory(idSupplier);
  }

  @Bean
  public GameService gameService(
      GameFactory gameFactory, GameRepository repository, GameEventPublisher publisher) {
    return GameService.builder()
        .factory(gameFactory)
        .formatter(new BoardFormatter())
        .repository(repository)
        .eventPublisher(publisher)
        .build();
  }

  @Bean
  public ApiConverter apiConverter() {
    return new ApiConverter();
  }

  @Bean
  public ApiUserConverter apiUserConverter() {
    return new ApiUserConverter();
  }

  @Bean
  public Jackson2ObjectMapperBuilderCustomizer objectMapperCustomizer() {
    return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL);
  }

  @Bean
  public AllowedOriginsSupplier corsAllowedOriginProvider(
      @Value("${cors.allowed.origins:}") String allowedOrigins) {
    return new AllowedOriginsSupplier(allowedOrigins);
  }

  @Bean
  public WebMvcConfigurer webMvcConfigurer(AllowedOriginsSupplier provider) {
    return new CorsWebMvcConfigurer(provider);
  }

  @Bean
  public BrokerConfig relayConfig(
      @Value("${broker.in.memory.enabled:false}") boolean inMemoryEnabled,
      @Value("${broker.ssl.enabled:true}") boolean sslEnabled,
      @Value("${broker.host:127.0.0.1}") String host,
      @Value("${broker.port:61613}") int port,
      @Value("${broker.client.login:guest}") String clientLogin,
      @Value("${broker.client.passcode:guest}") String clientPasscode,
      @Value("${broker.system.login:guest}") String systemLogin,
      @Value("${broker.system.passcode:guest}") String systemPasscode) {
    return BrokerConfig.builder()
        .inMemoryEnabled(inMemoryEnabled)
        .sslEnabled(sslEnabled)
        .host(host)
        .port(port)
        .clientLogin(clientLogin)
        .clientPasscode(clientPasscode)
        .systemLogin(systemLogin)
        .systemPasscode(systemPasscode)
        .build();
  }
}
