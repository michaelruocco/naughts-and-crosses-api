package uk.co.mruoc.nac.usecases;

import java.util.Collection;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserGroupService {

  private final ExternalUserService service;

  public Collection<String> getAll() {
    return service.getAllGroups().sorted().toList();
  }
}
