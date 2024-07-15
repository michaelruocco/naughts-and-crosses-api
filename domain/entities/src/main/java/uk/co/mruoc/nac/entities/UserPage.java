package uk.co.mruoc.nac.entities;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserPage {

  private final long total;
  private final Collection<User> items;
}
