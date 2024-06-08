package uk.co.mruoc.nac.api.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
@JsonPropertyOrder({"username", "firstName", "lastName", "email", "emailVerified"})
public class ApiCsvUser {
  private final String username;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final boolean emailVerified;
}
