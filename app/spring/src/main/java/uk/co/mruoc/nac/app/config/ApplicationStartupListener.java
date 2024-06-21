package uk.co.mruoc.nac.app.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import uk.co.mruoc.nac.usecases.ExternalUserSynchronizer;

@RequiredArgsConstructor
public class ApplicationStartupListener implements ApplicationListener<ContextRefreshedEvent> {

  private final ExternalUserSynchronizer synchronizer;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    synchronizer.synchronize();
  }
}
