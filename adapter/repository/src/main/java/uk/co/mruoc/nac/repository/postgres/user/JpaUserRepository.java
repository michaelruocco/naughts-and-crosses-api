package uk.co.mruoc.nac.repository.postgres.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uk.co.mruoc.nac.repository.postgres.dto.DbUser;

public interface JpaUserRepository
    extends JpaRepository<DbUser, String>, JpaSpecificationExecutor<DbUser> {
  // intentionally blank
}
