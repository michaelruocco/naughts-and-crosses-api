FROM eclipse-temurin:19-jre

ENV SERVER_PORT=80

COPY app/spring/build/libs/naughts-and-crosses-api-*.jar /opt/app.jar

CMD java \
  -Djava.security.egd=file:/dev/./urandom \
  -Dserver.port=${SERVER_PORT} \
  -Dcors.allowed.origins=${CORS_ALLOWED_ORIGINS} \
  -Dapp.database.url=${DATABASE_URL} \
  -Dapp.database.username=${DATABASE_USERNAME} \
  -Dapp.database.password=${DATABASE_PASSWORD} \
  -Dapp.database.driver=${DATABASE_DRIVER} \
  -Dkafka.listeners.enabled="${KAFKA_LISTENERS_ENABLED}" \
  -Dkafka.producers.enabled="${KAFKA_PRODUCERS_ENABLED}" \
  -Dkafka.bootstrap.servers="${KAFKA_BOOTSTRAP_SERVERS}" \
  -Dkafka.consumer.group.id="${KAFKA_CONSUMER_GROUP_ID}" \
  -Dkafka.client.id="${KAFKA_CLIENT_ID}" \
  -Dkafka.game.event.topic="${KAFKA_GAME_EVENT_TOPIC}" \
  -Dkafka.security.protocol="${KAFKA_SECURITY_PROTOCOL}" \
  -Dkafka.ssl.keystore="${KAFKA_SSL_KEYSTORE}" \
  -Dkafka.ssl.keystore.type="${KAFKA_SSL_KEYSTORE_TYPE}" \
  -Dkafka.ssl.keystore.password="${KAFKA_SSL_KEYSTORE_PASSWORD}" \
  -jar /opt/app.jar