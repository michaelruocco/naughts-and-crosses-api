package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {

  private final String id;
  private final String name;
  private final String email;
}
