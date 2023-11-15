package uk.co.mruoc.nac.environment.integrated.kafka;

import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class TestKafkaContainer extends KafkaContainer {

  public TestKafkaContainer() {
    super(DockerImageName.parse("confluentinc/cp-kafka:7.4.3"));
  }
}
