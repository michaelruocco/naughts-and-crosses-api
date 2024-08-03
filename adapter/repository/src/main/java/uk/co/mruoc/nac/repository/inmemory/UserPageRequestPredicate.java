package uk.co.mruoc.nac.repository.inmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserPageRequest;

@RequiredArgsConstructor
public class UserPageRequestPredicate implements Predicate<User> {

  private final Predicate<User> predicate;

  public UserPageRequestPredicate(UserPageRequest request) {
    this(toPredicate(request));
  }

  @Override
  public boolean test(User user) {
    return predicate.test(user);
  }

  private static Predicate<User> toPredicate(UserPageRequest request) {
    Collection<Predicate<User>> predicates = new ArrayList<>();
    predicates.add(new UserGroupsPredicate(request.getGroups()));
    request.getSearchTerm().map(SearchTermPredicate::new).ifPresent(predicates::add);
    return predicates.stream().reduce(x -> true, Predicate::and);
  }
}
