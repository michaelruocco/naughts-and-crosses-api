FROM eclipse-temurin:20-jre

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
  -Dbroker.ssl.enabled=${BROKER_SSL_ENABLED} \
  -Dbroker.host="${BROKER_HOST}" \
  -Dbroker.port="${BROKER_PORT}" \
  -Dbroker.client.login="${BROKER_CLIENT_LOGIN}" \
  -Dbroker.client.passcode="${BROKER_CLIENT_PASSCODE}" \
  -Dbroker.system.login="${BROKER_SYSTEM_LOGIN}" \
  -Dbroker.system.passcode="${BROKER_SYSTEM_PASSCODE}" \
  -Dauth.issuer.url="${AUTH_ISSUER_URL}" \
  -Daws.cognito.authCodeEndpoint="${AWS_COGNITO_AUTH_CODE_ENDPOINT}" \
  -Daws.cognito.userPoolId="${AWS_COGNITO_USER_POOL_ID}" \
  -Daws.cognito.userPoolClientId="${AWS_COGNITO_USER_POOL_CLIENT_ID}" \
  -Daws.cognito.regionName="${AWS_COGNITO_REGION_NAME}" \
  -Daws.cognito.endpointOverride="${AWS_COGNITO_ENDPOINT_OVERRIDE}" \
  -Daws.cognito.accessKeyId="${AWS_COGNITO_ACCESS_KEY_ID}" \
  -Daws.cognito.secretAccessKey="${AWS_COGNITO_SECRET_ACCESS_KEY}" \
  -Dclam.av.host="${CLAM_AV_HOST}" \
  -Dclam.av.port="${CLAM_AV_PORT}" \
  -Dclam.av.connect.timeout="${CLAM_AV_CONNECT_TIMEOUT}" \
  -Dclam.av.read.timeout="${CLAM_AV_READ_TIMEOUT}" \
  -jar /opt/app.jar