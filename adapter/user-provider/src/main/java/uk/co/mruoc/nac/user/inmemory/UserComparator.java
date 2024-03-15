package uk.co.mruoc.nac.user.inmemory;

import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.entities.User;

@RequiredArgsConstructor
public class UserComparator implements Comparator<User> {

  private final Comparator<String> idComparator;

  public UserComparator() {
    this(Comparator.nullsLast(Comparator.naturalOrder()));
  }

  @Override
  public int compare(User u1, User u2) {
    return idComparator.compare(u1.getId(), u2.getId());
  }
}
