package uk.co.mruoc.nac.app.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.co.mruoc.nac.api.converter.ApiConverter;
import uk.co.mruoc.nac.api.converter.ApiUserBatchConverter;
import uk.co.mruoc.nac.api.converter.ApiUserConverter;
import uk.co.mruoc.nac.app.config.security.CorsWebMvcConfigurer;
import uk.co.mruoc.nac.app.config.websocket.BrokerConfig;
import uk.co.mruoc.nac.usecases.BoardFormatter;
import uk.co.mruoc.nac.usecases.ExternalUserService;
import uk.co.mruoc.nac.usecases.ExternalUserSynchronizer;
import uk.co.mruoc.nac.usecases.GameEventPublisher;
import uk.co.mruoc.nac.usecases.GameFacade;
import uk.co.mruoc.nac.usecases.GameFactory;
import uk.co.mruoc.nac.usecases.GameRepository;
import uk.co.mruoc.nac.usecases.GameService;
import uk.co.mruoc.nac.usecases.IdSupplier;
import uk.co.mruoc.nac.usecases.PlayerFactory;
import uk.co.mruoc.nac.usecases.UserBatchExecutor;
import uk.co.mruoc.nac.usecases.UserBatchFactory;
import uk.co.mruoc.nac.usecases.UserBatchRepository;
import uk.co.mruoc.nac.usecases.UserBatchService;
import uk.co.mruoc.nac.usecases.UserCreator;
import uk.co.mruoc.nac.usecases.UserRepository;
import uk.co.mruoc.nac.usecases.UserService;
import uk.co.mruoc.nac.usecases.UserUpdater;

@Configuration
public class ApplicationConfig {

  @Bean
  public GameFacade gameFacade(PlayerFactory playerFactory, GameService gameService) {
    return GameFacade.builder().playerFactory(playerFactory).gameService(gameService).build();
  }

  @Bean
  public UserCreator userCreator(ExternalUserService externalService, UserRepository repository) {
    return UserCreator.builder().externalService(externalService).repository(repository).build();
  }

  @Bean
  public UserUpdater userUpdater(ExternalUserService externalService, UserRepository repository) {
    return UserUpdater.builder().externalService(externalService).repository(repository).build();
  }

  @Bean
  public UserBatchFactory userBatchFactory() {
    return new UserBatchFactory(UUID::randomUUID);
  }

  @Bean
  public UserBatchExecutor userBatchExecutor(UserCreator creator) {
    return new UserBatchExecutor(creator);
  }

  @Bean
  public UserBatchService userBatchService(
      UserBatchFactory factory, UserBatchRepository repository, UserBatchExecutor executor) {
    return UserBatchService.builder()
        .factory(factory)
        .repository(repository)
        .executor(executor)
        .build();
  }

  @Bean
  public UserService userService(UserCreator creator, UserRepository repository) {
    return UserService.builder().creator(creator).repository(repository).build();
  }

  @Bean
  public ApplicationStartupListener applicationStartupListener(
      ExternalUserSynchronizer synchronizer) {
    return new ApplicationStartupListener(synchronizer);
  }

  @Bean
  public ExternalUserSynchronizer externalUserSynchronizer(
      ExternalUserService externalUserService, UserRepository repository) {
    return ExternalUserSynchronizer.builder()
        .externalUserService(externalUserService)
        .repository(repository)
        .build();
  }

  @Bean
  public GameFactory gameFactory(IdSupplier idSupplier) {
    return new GameFactory(idSupplier);
  }

  @Bean
  public PlayerFactory playerFactory(UserService userService) {
    return new PlayerFactory(userService);
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
  public ApiUserBatchConverter apiUserBatchConverter() {
    return new ApiUserBatchConverter();
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
