package uk.co.mruoc.nac.app.rest;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.api.converter.ApiUserConverter;
import uk.co.mruoc.nac.api.dto.ApiCreateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUpdateUserRequest;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.api.dto.ApiUserPage;
import uk.co.mruoc.nac.api.dto.ApiUserPageRequest;
import uk.co.mruoc.nac.entities.UpsertUserRequest;
import uk.co.mruoc.nac.entities.UserPageRequest;
import uk.co.mruoc.nac.usecases.UserService;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService service;
  private final ApiUserConverter converter;

  @PostMapping
  public ApiUser create(@RequestBody ApiCreateUserRequest apiRequest) {
    UpsertUserRequest request = converter.toCreateUserRequest(apiRequest);
    service.create(request);
    return converter.toApiUser(service.getByUsername(request.getUsername()));
  }

  @PutMapping("/{username}")
  public ApiUser update(
      @PathVariable String username, @RequestBody ApiUpdateUserRequest apiRequest) {
    UpsertUserRequest request = converter.toUpsertUserRequest(username, apiRequest);
    service.update(request);
    return converter.toApiUser(service.getByUsername(request.getUsername()));
  }

  @GetMapping
  public Collection<ApiUser> getAll() {
    return service.getAll().map(converter::toApiUser).toList();
  }

  @PostMapping("/pages")
  public ApiUserPage createPage(@RequestBody ApiUserPageRequest apiRequest) {
    UserPageRequest request = converter.toRequest(apiRequest);
    var page = service.createPage(request);
    return converter.toApiUserPage(page);
  }

  @GetMapping("/{username}")
  public ApiUser getUser(@PathVariable String username) {
    return converter.toApiUser(service.getByUsername(username));
  }

  @DeleteMapping("/{username}")
  public ResponseEntity<Void> delete(@PathVariable String username) {
    service.delete(username);
    return ResponseEntity.noContent().build();
  }
}
