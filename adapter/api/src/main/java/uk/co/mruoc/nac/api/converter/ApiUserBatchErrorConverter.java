package uk.co.mruoc.nac.api.converter;

import java.util.Collection;
import uk.co.mruoc.nac.api.dto.ApiUserBatchError;
import uk.co.mruoc.nac.entities.UserBatchError;

public class ApiUserBatchErrorConverter {

  public Collection<ApiUserBatchError> toApiErrors(Collection<UserBatchError> errors) {
    return errors.stream().map(this::toApiError).toList();
  }

  public ApiUserBatchError toApiError(UserBatchError error) {
    return ApiUserBatchError.builder()
        .username(error.getUsername())
        .message(error.getMessage())
        .build();
  }
}
