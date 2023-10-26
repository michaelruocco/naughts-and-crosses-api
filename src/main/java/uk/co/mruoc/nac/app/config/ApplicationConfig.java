package uk.co.mruoc.nac.app.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.co.mruoc.nac.app.api.converter.ApiConverter;
import uk.co.mruoc.nac.app.domain.usecases.BoardFormatter;
import uk.co.mruoc.nac.app.domain.usecases.GameEventPublisher;
import uk.co.mruoc.nac.app.domain.usecases.GameFactory;
import uk.co.mruoc.nac.app.domain.usecases.GameRepository;
import uk.co.mruoc.nac.app.domain.usecases.GameService;
import uk.co.mruoc.nac.app.repository.InMemoryGameRepository;

@Configuration
public class ApplicationConfig {

    @Bean
    public GameRepository gameRepository() {
        return new InMemoryGameRepository();
    }

    @Bean
    public GameService gameService(GameRepository repository, GameEventPublisher publisher) {
        return GameService.builder()
                .factory(new GameFactory())
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
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/v1/*").allowedOrigins("http://localhost:3001");
            }
        };
    }
}
