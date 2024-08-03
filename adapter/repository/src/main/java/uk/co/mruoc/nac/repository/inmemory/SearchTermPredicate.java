package uk.co.mruoc.nac.repository.inmemory;

import io.micrometer.common.util.StringUtils;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
public class SearchTermPredicate implements Predicate<User> {

  private final String searchTerm;

  @Override
  public boolean test(User user) {
    if (StringUtils.isEmpty(searchTerm)) {
      return true;
    }
    return user.getUsername().contains(searchTerm) || user.getEmail().contains(searchTerm);
  }
}
