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
  -Dbroker.host="${BROKER_HOST}" \
  -Dbroker.port="${BROKER_PORT}" \
  -Dbroker.client.login="${BROKER_CLIENT_LOGIN}" \
  -Dbroker.client.passcode="${BROKER_CLIENT_PASSCODE}" \
  -Dbroker.system.login="${BROKER_SYSTEM_LOGIN}" \
  -Dbroker.system.passcode="${BROKER_SYSTEM_PASSCODE}" \
  -Dauth.issuer.url="${AUTH_ISSUER_URL}" \
  -Dkeycloak.admin.client.id="${KEYCLOAK_ADMIN_CLIENT_ID}" \
  -Dkeycloak.admin.client.secret="${KEYCLOAK_ADMIN_CLIENT_SECRET}" \
  -jar /opt/app.jar