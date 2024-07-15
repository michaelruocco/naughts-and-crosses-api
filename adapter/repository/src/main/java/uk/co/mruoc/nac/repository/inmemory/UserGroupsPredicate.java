package uk.co.mruoc.nac.repository.inmemory;

import java.util.Collection;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
public class UserGroupsPredicate implements Predicate<User> {

  private final Collection<String> groups;

  @Override
  public boolean test(User user) {
    if (groups.isEmpty()) {
      return true;
    }
    return user.getGroups().stream().anyMatch(groups::contains);
  }
}
