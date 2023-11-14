package uk.co.mruoc.nac.kafka;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.usecases.GameEventPublisher;

@Builder
@Slf4j
public class KafkaGameEventConsumer {

  private final KafkaGameConverter gameConverter;
  private final GameEventPublisher publisher;

  @KafkaListener(
      topics = "#{'${kafka.game.event.topic}'}",
      groupId = "#{'${kafka.consumer.group.id}'}")
  public void handleGameUpdate(ConsumerRecord<String, String> consumerRecord) {
    String message = consumerRecord.value();
    log.info(
        "consumed message from topic {} with key {} with value {}",
        consumerRecord.topic(),
        consumerRecord.key(),
        message);
    Game game = gameConverter.toGame(message);
    publisher.updated(game);
  }
}
