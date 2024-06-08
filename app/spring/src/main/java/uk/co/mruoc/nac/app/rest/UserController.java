package uk.co.mruoc.nac.app.rest;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.api.converter.ApiUserConverter;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.usecases.UserService;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService service;
  private final ApiUserConverter converter;

  @GetMapping
  public Collection<ApiUser> getAllUsers() {
    return service.getAll().stream().map(converter::toApiUser).toList();
  }

  @GetMapping("/{id}")
  public ApiUser getUser(@PathVariable String id) {
    return converter.toApiUser(service.get(id));
  }
}
