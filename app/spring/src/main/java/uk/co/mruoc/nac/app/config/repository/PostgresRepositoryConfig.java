package uk.co.mruoc.nac.app.config.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;
import uk.co.mruoc.nac.repository.postgres.PostgresIdSupplier;
import uk.co.mruoc.nac.repository.postgres.game.PostgresGameRepository;
import uk.co.mruoc.nac.repository.postgres.user.JpaUserRepository;
import uk.co.mruoc.nac.repository.postgres.user.PostgresUserBatchRepository;
import uk.co.mruoc.nac.repository.postgres.user.PostgresUserRepository;
import uk.co.mruoc.nac.usecases.IdSupplier;
import uk.co.mruoc.nac.usecases.UserBatchRepository;
import uk.co.mruoc.nac.usecases.UserRepository;

@Configuration
@ConditionalOnProperty(
    name = "repository.in.memory.enabled",
    havingValue = "false",
    matchIfMissing = true)
@EnableJpaRepositories("uk.co.mruoc.nac.repository.postgres")
@EntityScan("uk.co.mruoc.nac.repository.postgres.dto")
public class PostgresRepositoryConfig {

  @Bean
  public IdSupplier idSupplier(DataSource dataSource) {
    return new PostgresIdSupplier(dataSource);
  }

  @Bean
  public PostgresGameRepository postgresGameRepository(DataSource dataSource, ObjectMapper mapper) {
    return new PostgresGameRepository(dataSource, new JacksonJsonConverter(mapper));
  }

  @Bean
  public UserRepository postgresUserRepository(JpaUserRepository jpaUserRepository) {
    return new PostgresUserRepository(jpaUserRepository);
  }

  @Bean
  public UserBatchRepository postgresUserBatchRepository(
      DataSource dataSource, ObjectMapper mapper) {
    return new PostgresUserBatchRepository(dataSource, new JacksonJsonConverter(mapper));
  }
}
