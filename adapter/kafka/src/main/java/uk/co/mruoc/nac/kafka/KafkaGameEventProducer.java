package uk.co.mruoc.nac.kafka;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.usecases.GameEventPublisher;

@Builder
@Slf4j
public class KafkaGameEventProducer implements GameEventPublisher {

  private final KafkaTemplate<String, String> template;
  private final String topicName;
  private final KafkaGameConverter gameConverter;

  @Override
  public void updated(Game game) {
    String json = gameConverter.toJson(game);
    log.info("publishing game event {} to topic {}", json, topicName);
    template.send(topicName, json);
  }
}
