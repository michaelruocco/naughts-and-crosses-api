package uk.co.mruoc.nac.client;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UriFactory {

  private final String baseUrl;

  public String buildGetMinimalGameUri(long gameId) {
    return String.format("%s?minimal=true", buildGameUri(gameId));
  }

  public String buildTakeTurnUri(long gameId) {
    return String.format("%s/turns", buildGameUri(gameId));
  }

  public String buildGameUri(long gameId) {
    return String.format("%s/%d", buildGamesUri(), gameId);
  }

  public String buildGamesUri() {
    return String.format("%s/v1/games", baseUrl);
  }

  public String buildIdsUri() {
    return String.format("%s/v1/ids", baseUrl);
  }

  public String buildUserGroupsUri() {
    return String.format("%s/v1/user-groups", baseUrl);
  }

  public String buildUsersUri() {
    return String.format("%s/v1/users", baseUrl);
  }

  public String buildUserUri(String username) {
    return String.format("%s/%s", buildUsersUri(), username);
  }

  public String buildUserBatchesUri() {
    return String.format("%s/v1/users/batches", baseUrl);
  }

  public String buildUserBatchUri(String id) {
    return String.format("%s/%s", buildUserBatchesUri(), id);
  }

  public String buildExternalUserSynchronizationsUri() {
    return String.format("%s/v1/external-user-synchronizations", baseUrl);
  }
}
