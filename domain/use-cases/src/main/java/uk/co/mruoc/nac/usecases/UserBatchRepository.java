package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import uk.co.mruoc.nac.entities.UserBatch;

public interface UserBatchRepository {

  Optional<UserBatch> get(String id);

  void save(UserBatch batch);
}
