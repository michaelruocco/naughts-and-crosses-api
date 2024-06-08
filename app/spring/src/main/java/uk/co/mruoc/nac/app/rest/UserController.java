package uk.co.mruoc.nac.app.rest;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.api.converter.ApiUserConverter;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUpdateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UpdateUserRequest;
import uk.co.mruoc.nac.usecases.UserService;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService service;
  private final ApiUserConverter converter;

  @PostMapping
  public ApiUser create(ApiCreateUserRequest apiRequest) {
    CreateUserRequest request = converter.toCreateUserRequest(apiRequest);
    service.create(request);
    return converter.toApiUser(service.getByUsername(request.getUsername()));
  }

  @PutMapping("/{id}")
  public ApiUser update(@PathVariable String id, ApiUpdateUserRequest apiRequest) {
    UpdateUserRequest request = converter.toUpdateUserRequest(id, apiRequest);
    service.update(request);
    return converter.toApiUser(service.getById(request.getId()));
  }

  @GetMapping
  public Collection<ApiUser> getAllUsers() {
    return service.getAll().stream().map(converter::toApiUser).toList();
  }

  @GetMapping("/{id}")
  public ApiUser getUser(@PathVariable String id) {
    return converter.toApiUser(service.getById(id));
  }
}
