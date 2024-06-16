package uk.co.mruoc.nac.api.converter;

import java.io.InputStream;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiUserBatch;
import uk.co.mruoc.nac.entities.CreateUserRequest;
import uk.co.mruoc.nac.entities.UserBatch;

@RequiredArgsConstructor
public class ApiUserBatchConverter {

  private final ApiUserConverter userConverter;
  private final ApiCsvUserConverter csvConverter;

  public ApiUserBatchConverter() {
    this(new ApiUserConverter(), new ApiCsvUserConverter());
  }

  public Collection<CreateUserRequest> toCreateUserRequests(InputStream inputStream) {
    return csvConverter.toCreateUserRequests(inputStream);
  }

  public ApiUserBatch toApiUserBatch(UserBatch batch) {
    return ApiUserBatch.builder()
        .id(batch.getId())
        .requests(userConverter.toApiCreateUserRequests(batch.getRequests()))
        .users(userConverter.toApiUsers(batch.getUsers()))
        .errors(toApiErrors(batch.getErrors()))
        .complete(batch.isComplete())
        .build();
  }

  private static Collection<ApiUserBatch.Error> toApiErrors(Collection<UserBatch.Error> errors) {
    return errors.stream().map(ApiUserBatchConverter::toApiError).toList();
  }

  private static ApiUserBatch.Error toApiError(UserBatch.Error error) {
    return ApiUserBatch.Error.builder().username(error.username()).message(error.message()).build();
  }
}
