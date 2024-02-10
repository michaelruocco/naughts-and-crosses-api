package uk.co.mruoc.nac.app.config.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.nac.repository.inmemory.InMemoryGameRepository;
import uk.co.mruoc.nac.repository.inmemory.InMemoryIdSupplier;
import uk.co.mruoc.nac.usecases.GameRepository;
import uk.co.mruoc.nac.usecases.IdSupplier;

@Configuration
@EnableAutoConfiguration(
    exclude = {
      DataSourceAutoConfiguration.class,
      DataSourceTransactionManagerAutoConfiguration.class
    })
@ConditionalOnProperty(name = "repository.in.memory.enabled", havingValue = "true")
@Slf4j
public class InMemoryRepositoryConfig {

  @Bean
  public IdSupplier idSupplier() {
    return new InMemoryIdSupplier();
  }

  @Bean
  public GameRepository inMemoryGameRepository() {
    log.info("in memory repository configured");
    return new InMemoryGameRepository();
  }
}
