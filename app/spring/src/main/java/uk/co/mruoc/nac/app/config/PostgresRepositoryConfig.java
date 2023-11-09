package uk.co.mruoc.nac.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;
import uk.co.mruoc.nac.repository.postgres.PostgresGameRepository;

@Configuration
@ConditionalOnProperty(name = "in.memory.repository.enabled", havingValue = "false", matchIfMissing = true)
public class PostgresRepositoryConfig {

    @Bean
    public PostgresGameRepository postgresGameRepository(DataSource dataSource, ObjectMapper mapper) {
        return new PostgresGameRepository(dataSource, new JacksonJsonConverter(mapper));
    }
}
