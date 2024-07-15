package uk.co.mruoc.nac.repository.postgres.user;

import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.entities.UserPage;
import uk.co.mruoc.nac.entities.UserPageRequest;
import uk.co.mruoc.nac.repository.postgres.converter.DbUserConverter;
import uk.co.mruoc.nac.repository.postgres.dto.DbUser;
import uk.co.mruoc.nac.usecases.UserRepository;

@RequiredArgsConstructor
public class PostgresUserRepository implements UserRepository {

  private final JpaUserRepository jpaRepository;
  private final PageableFactory pageableFactory;
  private final SpecFactory specFactory;
  private final DbUserConverter converter;

  public PostgresUserRepository(JpaUserRepository jpaRepository) {
    this(jpaRepository, new PageableFactory(), new SpecFactory(), new DbUserConverter());
  }

  @Override
  public Optional<User> getByUsername(String username) {
    return jpaRepository.findById(username).map(converter::toUser);
  }

  @Override
  public Stream<User> getAll() {
    return jpaRepository.findAll().stream().map(converter::toUser);
  }

  @Override
  public UserPage getPage(UserPageRequest request) {
    Specification<DbUser> spec = specFactory.toSpec(request);
    Pageable pageable = pageableFactory.build(request.getPage());
    Page<DbUser> dbPage = jpaRepository.findAll(spec, pageable);
    return UserPage.builder()
        .total(dbPage.getTotalElements())
        .items(converter.toUsers(dbPage.toList()))
        .build();
  }

  @Override
  public void create(User user) {
    jpaRepository.save(converter.toDbUser(user));
  }

  @Override
  public void update(User user) {
    jpaRepository.save(converter.toDbUser(user));
  }

  @Override
  public void delete(String username) {
    jpaRepository.deleteById(username);
  }
}
