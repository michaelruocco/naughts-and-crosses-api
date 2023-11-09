package uk.co.mruoc.nac.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.usecases.IdSupplier;

@RestController
@RequestMapping("/v1/ids")
@RequiredArgsConstructor
public class IdController {

  private final IdSupplier idSupplier;

  @DeleteMapping
  public void resetIds() {
    idSupplier.reset();
  }
}
