package uk.co.mruoc.nac.api.converter;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiCsvUser;
import uk.co.mruoc.nac.entities.CreateUserRequest;

@RequiredArgsConstructor
public class ApiCsvUserConverter {

  private final CsvMapper mapper;

  public ApiCsvUserConverter() {
    this(new CsvMapper());
  }

  public Collection<CreateUserRequest> toCreateUserRequests(InputStream inputStream) {
    return toCreateUserRequests(toCsvUsers(inputStream));
  }

  private Collection<ApiCsvUser> toCsvUsers(InputStream inputStream) {
    try {
      CsvSchema schema = mapper.schemaFor(ApiCsvUser.class).withHeader();
      MappingIterator<ApiCsvUser> iterator =
          mapper.readerFor(ApiCsvUser.class).with(schema).readValues(inputStream);
      return iterator.readAll();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  private static Collection<CreateUserRequest> toCreateUserRequests(Collection<ApiCsvUser> lines) {
    return lines.stream().map(ApiCsvUserConverter::toCreateUserRequest).toList();
  }

  private static CreateUserRequest toCreateUserRequest(ApiCsvUser line) {
    return CreateUserRequest.builder()
        .username(line.getUsername())
        .firstName(line.getFirstName())
        .lastName(line.getLastName())
        .email(line.getEmail())
        .emailVerified(line.isEmailVerified())
        .groups(Set.of(line.getGroups().split("~")))
        .build();
  }
}
