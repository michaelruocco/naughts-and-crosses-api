package uk.co.mruoc.nac.api.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiCandidateGamePlayerMother {

  public static ApiCandidateGamePlayer admin() {
    return ApiCandidateGamePlayer.builder().username("admin").fullName("Admin User").build();
  }

  public static ApiCandidateGamePlayer user1() {
    return ApiCandidateGamePlayer.builder().username("user-1").fullName("User One").build();
  }

  public static ApiCandidateGamePlayer user2() {
    return ApiCandidateGamePlayer.builder().username("user-2").fullName("User Two").build();
  }
}
