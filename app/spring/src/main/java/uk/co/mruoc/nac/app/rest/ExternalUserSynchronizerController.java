package uk.co.mruoc.nac.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.usecases.ExternalUserSynchronizer;

@RestController
@RequestMapping("/v1/external-user-synchronizations")
@RequiredArgsConstructor
public class ExternalUserSynchronizerController {

  private final ExternalUserSynchronizer synchronizer;

  @PostMapping
  public ResponseEntity<Void> synchronize() {
    synchronizer.adminOnlySynchronize();
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/{username}")
  public ResponseEntity<Void> synchronize(@PathVariable String username) {
    synchronizer.adminOnlySynchronize(username);
    return ResponseEntity.noContent().build();
  }
}
