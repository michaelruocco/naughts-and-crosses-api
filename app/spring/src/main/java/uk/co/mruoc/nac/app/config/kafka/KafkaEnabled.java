package uk.co.mruoc.nac.app.config.kafka;

import org.springframework.boot.autoconfigure.condition.AnyNestedCondition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

public class KafkaEnabled extends AnyNestedCondition {

  public KafkaEnabled() {
    super(ConfigurationPhase.REGISTER_BEAN);
  }

  @ConditionalOnProperty(value = "kafka.listeners.enabled", havingValue = "true")
  static class ConsumerEnabled {
    // intentionally blank
  }

  @ConditionalOnProperty(value = "kafka.producers.enabled", havingValue = "true")
  static class ProducerEnabled {
    // intentionally blank
  }
}
