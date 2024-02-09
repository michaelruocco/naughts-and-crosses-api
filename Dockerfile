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
  -Dbroker.relay.host="${BROKER_RELAY_HOST}" \
  -Dbroker.relay.port="${BROKER_RELAY_PORT}" \
  -Dauth.issuer.url="${AUTH_ISSUER_URL}" \
  -jar /opt/app.jar