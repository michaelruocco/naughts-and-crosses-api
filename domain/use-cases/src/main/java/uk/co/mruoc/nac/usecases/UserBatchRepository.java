package uk.co.mruoc.nac.usecases;

import java.util.Optional;
import java.util.stream.Stream;
import uk.co.mruoc.nac.entities.UserBatch;

public interface UserBatchRepository {

  Optional<UserBatch> get(String id);

  void create(UserBatch batch);

  void update(UserBatch batch);

  Stream<UserBatch> getAll();

  void deleteAll();
}
