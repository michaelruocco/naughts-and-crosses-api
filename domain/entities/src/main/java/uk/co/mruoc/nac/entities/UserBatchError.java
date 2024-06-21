package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserBatchError {

  private final String username;
  private final String message;
}
