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
import uk.co.mruoc.nac.usecases.BoardFormatter;
import uk.co.mruoc.nac.usecases.GameEventPublisher;
import uk.co.mruoc.nac.usecases.GameFactory;
import uk.co.mruoc.nac.usecases.GameRepository;
import uk.co.mruoc.nac.usecases.GameService;
import uk.co.mruoc.nac.usecases.IdSupplier;
import uk.co.mruoc.nac.usecases.UserProvider;
import uk.co.mruoc.nac.usecases.UserService;

@Configuration
public class ApplicationConfig {

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
}
