package uk.co.mruoc.nac.app.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.co.mruoc.nac.api.converter.ApiConverter;
import uk.co.mruoc.nac.repository.inmemory.InMemoryGameRepository;
import uk.co.mruoc.nac.usecases.BoardFormatter;
import uk.co.mruoc.nac.usecases.DefaultIdSupplier;
import uk.co.mruoc.nac.usecases.GameEventPublisher;
import uk.co.mruoc.nac.usecases.GameFactory;
import uk.co.mruoc.nac.usecases.GameRepository;
import uk.co.mruoc.nac.usecases.GameService;
import uk.co.mruoc.nac.usecases.IdSupplier;

@Configuration
public class ApplicationConfig {

    @Bean
    public IdSupplier idSupplier() {
        return new DefaultIdSupplier();
    }

    @Bean
    public GameService gameService(IdSupplier idSupplier, GameRepository repository, GameEventPublisher publisher) {
        return GameService.builder()
                .factory(new GameFactory(idSupplier))
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
    public Jackson2ObjectMapperBuilderCustomizer objectMapperCustomizer() {
        return builder -> builder.serializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public AllowedOriginsSupplier corsAllowedOriginProvider(@Value("${cors.allowed.origins:}") String allowedOrigins) {
        return new AllowedOriginsSupplier(allowedOrigins);
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer(AllowedOriginsSupplier provider) {
        return new CorsWebMvcConfigurer(provider);
    }
}
