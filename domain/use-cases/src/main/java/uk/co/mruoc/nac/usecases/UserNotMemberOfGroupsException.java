package uk.co.mruoc.nac.usecases;

import java.util.Collection;
import java.util.List;

public class UserNotMemberOfGroupsException extends RuntimeException {

  public UserNotMemberOfGroupsException(String username, String... permittedGroups) {
    this(username, List.of(permittedGroups));
  }

  public UserNotMemberOfGroupsException(String username, Collection<String> permittedGroups) {
    super(String.format("user %s is not a member of groups %s", username, permittedGroups));
  }
}
