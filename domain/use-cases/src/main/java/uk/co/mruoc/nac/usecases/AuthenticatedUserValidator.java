package uk.co.mruoc.nac.usecases;

import java.util.Collection;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
public class AuthenticatedUserValidator {

  private final AuthenticatedUserSupplier supplier;

  public void validateIsAdminOrGamePlayer(Game game) {
    User user = supplier.get();
    if (user.isAdmin()) {
      return;
    }
    game.validateIsPlayer(user);
  }

  public void validateIsAdminOrUser(String username) {
    User user = supplier.get();
    if (user.isAdmin() || user.hasUsername(username)) {
      return;
    }
    throw new CannotAccessOtherUserException(user.getUsername(), username);
  }

  public void validateIsAdmin() {
    validateUserMemberOfAtLeastOne("admin");
  }

  public void validateUserMemberOfAtLeastOne(String... groups) {
    validateUserMemberOfAtLeastOne(Set.of(groups));
  }

  public void validateUserMemberOfAtLeastOne(Collection<String> groups) {
    User user = supplier.get();
    if (user.isMemberOfAtLeastOneGroup(groups)) {
      return;
    }
    throw new UserNotMemberOfGroupsException(user.getUsername(), groups);
  }
}
