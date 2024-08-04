package uk.co.mruoc.nac.repository.inmemory;

import io.micrometer.common.util.StringUtils;
import java.util.function.Predicate;
import java.util.stream.Stream;
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
    return Stream.of(user.getUsername(), user.getEmail(), user.getFirstName(), user.getLastName())
        .anyMatch(this::containsSearchTerm);
  }

  private boolean containsSearchTerm(String value) {
    if (StringUtils.isEmpty(value)) {
      return false;
    }
    return value.toLowerCase().contains(searchTerm.toLowerCase());
  }
}
