package uk.co.mruoc.nac.app.config.kafka;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import uk.co.mruoc.nac.kafka.KafkaGameConverter;
import uk.co.mruoc.nac.kafka.KafkaGameEventProducer;

@Configuration
@ConditionalOnProperty(value = "kafka.producers.enabled", havingValue = "true")
@Slf4j
public class KafkaProducerConfig {

  @Bean
  public ProducerFactory<String, String> producerFactory(
      @Value("${kafka.bootstrap.servers}") String bootstrapServers,
      @Value("${kafka.client.id}") String clientId,
      KafkaSslConfig sslConfig) {
    log.info(
        "setting up producer factory with boostrap servers {} and client id {}",
        bootstrapServers,
        clientId);
    Map<String, Object> props = new HashMap<>();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(ProducerConfig.CLIENT_ID_CONFIG, clientId);
    props.putAll(sslConfig.buildSslConfig());
    return new DefaultKafkaProducerFactory<>(props);
  }

  @Bean
  public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String> factory) {
    return new KafkaTemplate<>(factory);
  }

  @Bean
  @Primary
  public KafkaGameEventProducer kafkaGameEventProducer(
      KafkaTemplate<String, String> template,
      @Value("${kafka.game.event.topic:game-event}") String topicName,
      KafkaGameConverter gameConverter) {
    return KafkaGameEventProducer.builder()
        .template(template)
        .topicName(topicName)
        .gameConverter(gameConverter)
        .build();
  }
}
