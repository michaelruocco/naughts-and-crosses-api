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
  private final ApiUserBatchErrorConverter errorConverter;

  public ApiUserBatchConverter() {
    this(new ApiUserConverter(), new ApiCsvUserConverter(), new ApiUserBatchErrorConverter());
  }

  public Collection<CreateUserRequest> toCreateUserRequests(InputStream inputStream) {
    return csvConverter.toCreateUserRequests(inputStream);
  }

  public ApiUserBatch toApiUserBatch(UserBatch batch) {
    return ApiUserBatch.builder()
        .id(batch.getId())
        .requests(userConverter.toApiCreateUserRequests(batch.getRequests()))
        .users(userConverter.toApiUsers(batch.getUsers()))
        .errors(errorConverter.toApiErrors(batch.getErrors()))
        .createdAt(batch.getCreatedAt())
        .updatedAt(batch.getUpdatedAt())
        .complete(batch.isComplete())
        .build();
  }
}
