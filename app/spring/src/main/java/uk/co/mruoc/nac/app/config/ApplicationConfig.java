package uk.co.mruoc.nac.app.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Clock;
import java.util.UUID;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.co.mruoc.nac.api.converter.ApiConverter;
import uk.co.mruoc.nac.api.converter.ApiTokenConverter;
import uk.co.mruoc.nac.api.converter.ApiUserBatchConverter;
import uk.co.mruoc.nac.api.converter.ApiUserConverter;
import uk.co.mruoc.nac.app.config.security.CorsWebMvcConfigurer;
import uk.co.mruoc.nac.app.config.websocket.BrokerConfig;
import uk.co.mruoc.nac.usecases.AuthenticatedUserSupplier;
import uk.co.mruoc.nac.usecases.AuthenticatedUserValidator;
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
import uk.co.mruoc.nac.usecases.UserDeleter;
import uk.co.mruoc.nac.usecases.UserFinder;
import uk.co.mruoc.nac.usecases.UserRepository;
import uk.co.mruoc.nac.usecases.UserService;
import uk.co.mruoc.nac.usecases.UserUpdater;
import uk.co.mruoc.nac.usecases.UserUpserter;
import uk.co.mruoc.nac.usecases.VirusScanner;

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
  public UserDeleter userDeleter(
      ExternalUserService externalUserService, UserRepository repository) {
    return UserDeleter.builder()
        .externalService(externalUserService)
        .repository(repository)
        .build();
  }

  @Bean
  public Supplier<UUID> randomUuidSupplier() {
    return UUID::randomUUID;
  }

  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }

  @Bean
  public UserBatchFactory userBatchFactory(Supplier<UUID> uuidSupplier, Clock clock) {
    return UserBatchFactory.builder().uuidSupplier(uuidSupplier).clock(clock).build();
  }

  @Bean
  public UserBatchExecutor userBatchExecutor(
      UserUpserter upserter, UserBatchRepository repository, Clock clock) {
    return UserBatchExecutor.builder()
        .upserter(upserter)
        .repository(repository)
        .clock(clock)
        .build();
  }

  @Bean
  public UserFinder userFinder(UserRepository repository) {
    return new UserFinder(repository);
  }

  @Bean
  public UserUpserter userUpserter(UserFinder finder, UserCreator creator, UserUpdater updater) {
    return UserUpserter.builder().finder(finder).creator(creator).updater(updater).build();
  }

  @Bean
  public UserBatchService userBatchService(
      VirusScanner virusScanner,
      AuthenticatedUserValidator userValidator,
      UserBatchFactory factory,
      UserBatchRepository repository,
      UserBatchExecutor executor) {
    return UserBatchService.builder()
        .virusScanner(virusScanner)
        .userValidator(userValidator)
        .factory(factory)
        .repository(repository)
        .executor(executor)
        .build();
  }

  @Bean
  public UserService userService(
      AuthenticatedUserValidator userValidator,
      UserCreator creator,
      UserUpdater updater,
      UserDeleter deleter,
      UserFinder finder) {
    return UserService.builder()
        .userValidator(userValidator)
        .creator(creator)
        .updater(updater)
        .deleter(deleter)
        .finder(finder)
        .build();
  }

  @Bean
  public ApplicationStartupListener applicationStartupListener(
      ExternalUserSynchronizer synchronizer) {
    return new ApplicationStartupListener(synchronizer);
  }

  @Bean
  public ExternalUserSynchronizer externalUserSynchronizer(
      AuthenticatedUserValidator userValidator,
      ExternalUserService externalUserService,
      UserRepository repository) {
    return ExternalUserSynchronizer.builder()
        .userValidator(userValidator)
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
      AuthenticatedUserValidator userValidator,
      GameFactory gameFactory,
      GameRepository repository,
      GameEventPublisher publisher) {
    return GameService.builder()
        .userValidator(userValidator)
        .factory(gameFactory)
        .formatter(new BoardFormatter())
        .repository(repository)
        .eventPublisher(publisher)
        .build();
  }

  @Bean
  public ApiConverter apiConverter(AuthenticatedUserSupplier userSupplier) {
    return new ApiConverter(userSupplier);
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
  public ApiTokenConverter apiTokenConverter() {
    return new ApiTokenConverter();
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
