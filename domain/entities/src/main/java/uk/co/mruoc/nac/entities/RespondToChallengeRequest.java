package uk.co.mruoc.nac.entities;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Data
public class RespondToChallengeRequest {
  private final String challenge;
  private final String session;
  private final String username;
  private final String userCode;
}
