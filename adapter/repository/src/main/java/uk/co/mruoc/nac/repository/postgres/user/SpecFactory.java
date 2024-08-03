package uk.co.mruoc.nac.repository.postgres.user;

import java.util.Collection;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import uk.co.mruoc.nac.entities.UserPageRequest;
import uk.co.mruoc.nac.repository.postgres.dto.DbUser;

public class SpecFactory {

  public Specification<DbUser> toFindByUsername(String username) {
    return (user, query, builder) -> builder.equal(user.get("username"), username);
  }

  public Specification<DbUser> toSpec(UserPageRequest request) {
    Specification<DbUser> spec = Specification.where(null);
    Collection<String> groups = request.getGroups();
    if (!groups.isEmpty()) {
      spec = spec.and(toInGroupSpec(groups));
    }
    Optional<String> searchTerm = request.getSearchTerm();
    if (searchTerm.isPresent()) {
      spec = spec.and(toSearchTermSpec(searchTerm.get()));
    }
    return spec;
  }

  private static Specification<DbUser> toInGroupSpec(Collection<String> groups) {
    return (user, query, builder) -> builder.in(user.join("groups")).value(groups);
  }

  private Specification<DbUser> toSearchTermSpec(String searchTerm) {
    return toUsernameLike(searchTerm).or(toEmailLike(searchTerm));
  }

  private static Specification<DbUser> toUsernameLike(String searchTerm) {
    return toLike("username", searchTerm);
  }

  private static Specification<DbUser> toEmailLike(String searchTerm) {
    return toLike("email", searchTerm);
  }

  private static Specification<DbUser> toLike(String fieldName, String searchTerm) {
    return (user, query, builder) -> builder.like(user.get(fieldName), toLike(searchTerm));
  }

  private static String toLike(String value) {
    return String.format("%%%s%%", value);
  }
}
