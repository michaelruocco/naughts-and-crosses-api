package uk.co.mruoc.nac.app.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.nac.repository.inmemory.InMemoryGameRepository;
import uk.co.mruoc.nac.usecases.GameRepository;

@Configuration
@EnableAutoConfiguration(
    exclude = {
      DataSourceAutoConfiguration.class,
      DataSourceTransactionManagerAutoConfiguration.class
    })
@ConditionalOnProperty(name = "in.memory.repository.enabled", havingValue = "true")
@Slf4j
public class InMemoryRepositoryConfig {

  @Bean
  public GameRepository inMemoryGameRepository() {
    log.info("in memory repository configured");
    return new InMemoryGameRepository();
  }
}
