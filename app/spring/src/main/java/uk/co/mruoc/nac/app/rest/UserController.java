package uk.co.mruoc.nac.app.rest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.co.mruoc.nac.api.converter.ApiUserConverter;
import uk.co.mruoc.nac.api.dto.ApiUser;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.usecases.UserService;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService service;
  private final ApiUserConverter converter;

  @PostMapping
  public ResponseEntity<Void> createUsers(@RequestParam("data") MultipartFile csv) {
    try {
      Collection<CreateUserRequest> requests = converter.toCreateUserRequests(csv.getInputStream());
      service.create(requests);
      return ResponseEntity.noContent().build();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @GetMapping
  public Collection<ApiUser> getAllUsers() {
    return service.getAll().map(converter::toApiUser).toList();
  }

  @GetMapping("/{id}")
  public ApiUser getUser(@PathVariable String id) {
    return converter.toApiUser(service.get(id));
  }
}
