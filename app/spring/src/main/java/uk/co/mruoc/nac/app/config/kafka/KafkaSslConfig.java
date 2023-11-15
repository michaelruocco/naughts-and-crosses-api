package uk.co.mruoc.nac.app.config.kafka;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.common.config.SslConfigs;

@Builder
@Data
@Slf4j
public class KafkaSslConfig {

  private final String securityProtocol;
  private final String keystorePath;
  private final String keystoreType;
  private final String keystorePassword;
  private final boolean disableHostnameVerification;

  public Map<String, Object> buildSslConfig() {
    Map<String, Object> props = new HashMap<>();
    props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
    log.info("setting up kafka ssl config with security protocol {}", securityProtocol);
    if (isSsl()) {
      return configureSsl(props);
    }
    return props;
  }

  private boolean isSsl() {
    return "SSL".equals(securityProtocol);
  }

  private Map<String, Object> configureSsl(Map<String, Object> originalProps) {
    Map<String, Object> props = new HashMap<>(originalProps);
    props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, keystorePath);
    props.put(SslConfigs.SSL_KEYSTORE_TYPE_CONFIG, keystoreType);
    props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, keystorePassword);
    props.putAll(disableHostnameVerificationIfConfigured());
    return props;
  }

  private Map<String, Object> disableHostnameVerificationIfConfigured() {
    Map<String, Object> props = new HashMap<>();
    if (disableHostnameVerification) {
      log.warn("hostname verification disabled this should only be used locally");
      props.put(SslConfigs.SSL_ENDPOINT_IDENTIFICATION_ALGORITHM_CONFIG, "");
    }
    return props;
  }
}
