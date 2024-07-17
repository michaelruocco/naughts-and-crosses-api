package uk.co.mruoc.nac.app.rest;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.usecases.UserGroupService;

@RestController
@RequestMapping("/v1/user-groups")
@RequiredArgsConstructor
public class UserGroupController {

  private final UserGroupService service;

  @GetMapping
  public Collection<String> getAll() {
    return service.getAll();
  }
}
