package uk.co.mruoc.nac.environment.integrated.kafka;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;

import java.util.Arrays;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class TestKafkaContainer extends KafkaContainer {

  public TestKafkaContainer() {
    super(DockerImageName.parse("confluentinc/cp-kafka:7.4.3"));
  }

  public void createTopics(String... topics) {
    log.info("creating topics {}", Arrays.toString(topics));
    var newTopics = Arrays.stream(topics).map(topic -> new NewTopic(topic, 1, (short) 1)).toList();
    try (var admin = AdminClient.create(Map.of(BOOTSTRAP_SERVERS_CONFIG, getKafkaBrokersUrl()))) {
      admin.createTopics(newTopics);
    }
  }

  private String getKafkaBrokersUrl() {
    return String.format("http://localhost:%d", getFirstMappedPort());
  }
}
