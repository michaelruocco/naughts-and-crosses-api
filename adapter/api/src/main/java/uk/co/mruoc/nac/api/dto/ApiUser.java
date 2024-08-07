package uk.co.mruoc.nac.api.dto;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Builder
@Data
public class ApiUser {

  private final String id;
  private final String username;
  private final String fullName;
  private final String firstName;
  private final String lastName;
  private final String email;
  private final Boolean emailVerified;
  private final String status;
  private final Collection<String> groups;
}
