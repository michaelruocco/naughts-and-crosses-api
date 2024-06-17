package uk.co.mruoc.nac.app.rest;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import uk.co.mruoc.nac.api.converter.ApiUserBatchConverter;
import uk.co.mruoc.nac.api.dto.ApiUserBatch;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UserBatch;
import uk.co.mruoc.nac.usecases.UserBatchService;

@RestController
@RequestMapping("/v1/users/batches")
@RequiredArgsConstructor
public class UserBatchController {

  private final UserBatchService service;
  private final ApiUserBatchConverter converter;

  @PostMapping
  public ResponseEntity<ApiUserBatch> create(@RequestParam("data") MultipartFile csv) {
    try {
      Collection<CreateUserRequest> requests = converter.toCreateUserRequests(csv.getInputStream());
      UserBatch batch = service.create(requests);
      return ResponseEntity.accepted().body(converter.toApiUserBatch(batch));
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  @GetMapping("/{id}")
  public ApiUserBatch get(@PathVariable String id) {
    return converter.toApiUserBatch(service.get(id));
  }

  @GetMapping
  public Collection<ApiUserBatch> getAll() {
    return service.getAll().map(converter::toApiUserBatch).toList();
  }

  @DeleteMapping
  public void deleteAll() {
    service.deleteAll();
  }
}
