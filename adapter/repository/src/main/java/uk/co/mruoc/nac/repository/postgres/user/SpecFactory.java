package uk.co.mruoc.nac.repository.postgres.user;

import java.util.Collection;
import org.springframework.data.jpa.domain.Specification;
import uk.co.mruoc.nac.entities.UserPageRequest;
import uk.co.mruoc.nac.repository.postgres.dto.DbUser;

public class SpecFactory {

  public Specification<DbUser> toSpec(UserPageRequest request) {
    Collection<String> groups = request.getGroups();
    if (groups.isEmpty()) {
      return Specification.where(null);
    }
    return toInGroupSpec(groups);
  }

  private Specification<DbUser> toInGroupSpec(Collection<String> groups) {
    return (user, query, builder) -> builder.in(user.join("groups")).value(groups);
  }
}
