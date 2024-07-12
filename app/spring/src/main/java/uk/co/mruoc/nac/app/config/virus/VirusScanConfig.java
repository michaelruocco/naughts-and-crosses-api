package uk.co.mruoc.nac.app.config.virus;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.mruoc.nac.usecases.VirusScanner;
import uk.co.mruoc.nac.virus.clamav.ClamAvVirusScanner;
import uk.co.mruoc.nac.virus.clamav.ClamAvVirusScannerConfig;
import uk.co.mruoc.nac.virus.inmemory.StubVirusScanner;

@Configuration
public class VirusScanConfig {

  @ConditionalOnProperty(
      name = "stub.virus.scan.enabled",
      havingValue = "false",
      matchIfMissing = true)
  @Bean
  public VirusScanner clamAvVirusScanner(
      @Value("${clam.av.host:localhost}") String host,
      @Value("${clam.av.port:3310}") int port,
      @Value("${clam.av.connect.timeout:2}") int connectTimeout,
      @Value("${clam.av.read.timeout:20}") int readTimeout) {
    ClamAvVirusScannerConfig config =
        ClamAvVirusScannerConfig.builder()
            .host(host)
            .port(port)
            .connectTimeout(Duration.ofSeconds(connectTimeout))
            .readTimeout(Duration.ofSeconds(readTimeout))
            .build();
    return new ClamAvVirusScanner(config);
  }

  @ConditionalOnProperty(name = "stub.virus.scan.enabled", havingValue = "true")
  @Bean
  public VirusScanner stubVirusScanner() {
    return new StubVirusScanner();
  }
}
