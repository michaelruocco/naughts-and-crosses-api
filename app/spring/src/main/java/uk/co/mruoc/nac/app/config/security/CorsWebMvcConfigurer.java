package uk.co.mruoc.nac.app.config.security;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import uk.co.mruoc.nac.app.config.AllowedOriginsSupplier;

@RequiredArgsConstructor
@Slf4j
public class CorsWebMvcConfigurer implements WebMvcConfigurer {

  private final AllowedOriginsSupplier originsSupplier;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    String[] origins = originsSupplier.get();
    log.info("cors allowed from origins {}", Arrays.toString(origins));
    registry.addMapping("/**").allowedMethods("*").allowedOrigins(origins);
  }
}
