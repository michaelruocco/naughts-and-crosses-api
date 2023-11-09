package uk.co.mruoc.nac.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;
import uk.co.mruoc.nac.repository.inmemory.InMemoryGameRepository;
import uk.co.mruoc.nac.repository.postgres.PostgresGameRepository;
import uk.co.mruoc.nac.usecases.GameRepository;

import javax.sql.DataSource;

@Configuration
public class RepositoryConfig {

    @ConditionalOnMissingBean
    public GameRepository inMemoryGameRepository() {
        return new InMemoryGameRepository();
    }

    @Bean
    public PostgresGameRepository postgresGameRepository(DataSource dataSource, ObjectMapper mapper) {
        return new PostgresGameRepository(dataSource, new JacksonJsonConverter(mapper));
    }
}
