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

  public String buildUsersUri() {
    return String.format("%s/v1/users", baseUrl);
  }
}
