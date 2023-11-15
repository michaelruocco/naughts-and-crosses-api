package uk.co.mruoc.nac.app.config.kafka;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import uk.co.mruoc.nac.kafka.KafkaGameConverter;
import uk.co.mruoc.nac.kafka.KafkaGameEventConsumer;
import uk.co.mruoc.nac.usecases.GameEventPublisher;

@Configuration
@ConditionalOnProperty(value = "kafka.listeners.enabled", havingValue = "true")
@Slf4j
public class KafkaConsumerConfig {

  @Bean
  public ConsumerFactory<String, String> consumerFactory(
      @Value("${kafka.bootstrap.servers}") String bootstrapServers,
      @Value("${kafka.consumer.group.id}") String consumerGroupId,
      KafkaSslConfig sslConfig) {
    log.info(
        "setting up consumer factory with boostrap servers {} and consumer group id {}",
        bootstrapServers,
        consumerGroupId);
    Map<String, Object> props = new HashMap<>();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    props.putAll(sslConfig.buildSslConfig());
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
      ConsumerFactory<String, String> consumerFactory) {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(consumerFactory);
    return factory;
  }

  @Bean
  public KafkaGameEventConsumer gameEventConsumer(
      KafkaGameConverter gameConverter,
      @Qualifier("webSocketGameEventPublisher") GameEventPublisher publisher) {
    return KafkaGameEventConsumer.builder()
        .gameConverter(gameConverter)
        .publisher(publisher)
        .build();
  }
}
