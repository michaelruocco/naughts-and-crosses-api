package uk.co.mruoc.nac.entities;

import java.util.Comparator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserIdComparator implements Comparator<User> {

  private final Comparator<String> idComparator;

  public UserIdComparator() {
    this(Comparator.nullsLast(Comparator.naturalOrder()));
  }

  @Override
  public int compare(User u1, User u2) {
    return idComparator.compare(u1.getId(), u2.getId());
  }
}
