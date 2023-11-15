package uk.co.mruoc.nac.app.config.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import uk.co.mruoc.json.jackson.JacksonJsonConverter;
import uk.co.mruoc.nac.api.converter.ApiGameConverter;
import uk.co.mruoc.nac.kafka.KafkaGameConverter;

@EnableKafka
@Configuration
@Conditional(KafkaEnabled.class)
public class CommonKafkaConfig {

  @Bean
  public KafkaSslConfig kafkaSslConfig(
      @Value("${kafka.security.protocol}") String securityProtocol,
      @Value("${kafka.ssl.keystore:}") String keystorePath,
      @Value("${kafka.ssl.keystore.type:}") String keystoreType,
      @Value("${kafka.ssl.keystore.password:}") String keystorePassword,
      @Value("${kafka.ssl.disable.hostname.verification:false}")
          boolean disableHostnameVerification) {
    return KafkaSslConfig.builder()
        .securityProtocol(securityProtocol)
        .keystorePath(keystorePath)
        .keystoreType(keystoreType)
        .keystorePassword(keystorePassword)
        .disableHostnameVerification(disableHostnameVerification)
        .build();
  }

  @Bean
  public KafkaGameConverter gameConverter(ObjectMapper mapper) {
    return KafkaGameConverter.builder()
        .apiGameConverter(new ApiGameConverter())
        .jsonConverter(new JacksonJsonConverter(mapper))
        .build();
  }
}
