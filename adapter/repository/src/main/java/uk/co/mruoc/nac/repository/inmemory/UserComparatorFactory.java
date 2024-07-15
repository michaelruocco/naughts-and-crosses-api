package uk.co.mruoc.nac.repository.inmemory;

import java.util.Comparator;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import uk.co.mruoc.nac.entities.User;

@Slf4j
public class UserComparatorFactory extends AbstractComparatorFactory<User> {

  public UserComparatorFactory() {
    super(buildFieldComparators(), Comparator.comparing(User::getUsername));
  }

  private static Map<String, Comparator<User>> buildFieldComparators() {
    return Map.of("username", Comparator.comparing(User::getUsername));
  }
}
