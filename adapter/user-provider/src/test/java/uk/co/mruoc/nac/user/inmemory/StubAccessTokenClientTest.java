package uk.co.mruoc.nac.user.inmemory;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.mock;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import java.util.function.Supplier;
import org.junit.jupiter.api.Test;
import uk.co.mruoc.nac.entities.AccessTokenResponse;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.usecases.AccessTokenClient;
import uk.co.mruoc.nac.usecases.CreateAccessTokenFailedException;
import uk.co.mruoc.nac.usecases.RefreshAccessTokenFailedException;

class StubAccessTokenClientTest {

  private static final Instant NOW = Instant.parse("2024-07-12T20:25:01.001Z");
  private static final UUID ID = UUID.fromString("44e5685b-189a-47f2-a8bd-0c7a0dd4dcb2");

  private final Clock clock = Clock.fixed(NOW, UTC);
  private final Supplier<UUID> uuidSupplier = () -> ID;
  private final JwtValidator validator = mock(JwtValidator.class);

  private final AccessTokenClient service =
      new StubAccessTokenClient(clock, uuidSupplier, validator);

  @Test
  void createShouldThrowExceptionIfUserDoesNotExist() {
    CreateTokenRequest request =
        CreateTokenRequest.builder().username("unknown-user").password("any-password").build();

    Throwable error = catchThrowable(() -> service.create(request));

    assertThat(error)
        .isInstanceOf(CreateAccessTokenFailedException.class)
        .hasMessage("create access token failed for %s", request.getUsername());
  }

  @Test
  void createShouldThrowExceptionIfPasswordIsNotCorrect() {
    CreateTokenRequest request =
        CreateTokenRequest.builder().username("admin").password("incorrect-password").build();

    Throwable error = catchThrowable(() -> service.create(request));

    assertThat(error)
        .isInstanceOf(CreateAccessTokenFailedException.class)
        .hasMessage("create access token failed for %s", request.getUsername());
  }

  @Test
  void createShouldReturnTokenIfPasswordIsCorrect() {
    CreateTokenRequest request =
        CreateTokenRequest.builder().username("admin").password("pwd").build();

    AccessTokenResponse response = service.create(request);

    assertThat(response.getAccessToken())
        .isEqualTo(
            "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9."
                + "eyJzdWIiOiI0MmJmZGFkOS1jNjZhLTRkNWEtYWE0OC1hOTdkNmQzNTc0YWYiLCJpc3MiOiJuYWMtc3R1Yi10b2tlbi1zZXJ2a"
                + "WNlIiwidXNlcm5hbWUiOiJhZG1pbiIsInVuaXF1ZUlkIjoiNDRlNTY4NWItMTg5YS00N2YyLWE4YmQtMGM3YTBkZDRkY2IyIi"
                + "wiaWF0IjoxNzIwODE1OTAxLCJleHAiOjE3MjA4MTY4MDF9.KHrSRQvHyMk8hMzFpk_WaN7IpxTeu0fO8sU5h0vGSQeq6QB5rk"
                + "89K3pTqBoQauuhP0IxG2EuqNRxmOQGzyg16UDtNfwPT7Ofl0glqv63wU0IxZTy_Byq-d7lWWpxUZy1ra3HSwXHn6k2anoGoB-"
                + "H3fsba0OwmbkyA6loFYRX1PSl6-abQzu8Nkx9JCV_CLK45IkL0hK5BmC5E1Ud-dfnXq2JdxvRnjHx7zuxC2H5747a0MAlKNZG"
                + "d5lVw9nlv_v0hDrmKhD5QlEAuxukmMEWbjihQsncQz9f9fw00xe6J8OUe_DwfzSW9_htk6-7rERHqi1NtNfRSj_6sajZm4E5jg");
    assertThat(response.getRefreshToken())
        .isEqualTo(
            "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9."
                + "eyJzdWIiOiI0MmJmZGFkOS1jNjZhLTRkNWEtYWE0OC1hOTdkNmQzNTc0YWYiLCJpc3MiOiJuYWMtc3R1Yi10b2tlbi1zZXJ2a"
                + "WNlIiwidXNlcm5hbWUiOiJhZG1pbiIsInVuaXF1ZUlkIjoiNDRlNTY4NWItMTg5YS00N2YyLWE4YmQtMGM3YTBkZDRkY2IyIi"
                + "wiaWF0IjoxNzIwODE1OTAxLCJleHAiOjE3MjA4MTk1MDF9.NmcqW854imm1YSl3p7xwcHwnSAGvIdl-_CrJji-ro83pBUVSD2"
                + "MKaYJ1OmWM1DCAm-khDzF_rGTbh4S7Nt92E3OOs_UJ2SvNPy8ZK_F2OQGqwYiHUj1vGwVnDUUG_p1RNSw09aGwiMDQB9WX1M-"
                + "CDj0oStRxBA_kt-AtfSXeCChX-hLLiEC8TFjm9qIm_2XKhESDy5Y2RJtoMt_b7NJFWxRd8ZCf_ctTxPIAZw5KI0MuAwKkwjjN"
                + "BaB3k-okXBY1s00JoD12FZQSQpnvhdAnbJ1Jw_obsiiTeKUDa7JCLYLyT3s6CiEJbf312UGZkR2FxVSY80xT9ZGs4H2MNnw60g");
  }

  @Test
  void refreshShouldThrowExceptionIfRefreshTokenDoesNotExist() {
    RefreshTokenRequest request = new RefreshTokenRequest("invalid-refresh-token");

    Throwable error = catchThrowable(() -> service.refresh(request));

    assertThat(error)
        .isInstanceOf(RefreshAccessTokenFailedException.class)
        .hasMessage("refresh access token failed for token %s", request.getRefreshToken());
  }

  @Test
  void refreshShouldGenerateAccessTokenIfRefreshTokenIsValid() {
    String refreshToken = givenValidRefreshToken();
    RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

    AccessTokenResponse response = service.refresh(request);

    assertThat(response.getAccessToken())
        .isEqualTo(
            "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9."
                + "eyJzdWIiOiI0MmJmZGFkOS1jNjZhLTRkNWEtYWE0OC1hOTdkNmQzNTc0YWYiLCJpc3MiOiJuYWMtc3R1Yi10b2tlbi1zZXJ2a"
                + "WNlIiwidXNlcm5hbWUiOiJhZG1pbiIsInVuaXF1ZUlkIjoiNDRlNTY4NWItMTg5YS00N2YyLWE4YmQtMGM3YTBkZDRkY2IyIi"
                + "wiaWF0IjoxNzIwODE1OTAxLCJleHAiOjE3MjA4MTY4MDF9.KHrSRQvHyMk8hMzFpk_WaN7IpxTeu0fO8sU5h0vGSQeq6QB5rk"
                + "89K3pTqBoQauuhP0IxG2EuqNRxmOQGzyg16UDtNfwPT7Ofl0glqv63wU0IxZTy_Byq-d7lWWpxUZy1ra3HSwXHn6k2anoGoB-"
                + "H3fsba0OwmbkyA6loFYRX1PSl6-abQzu8Nkx9JCV_CLK45IkL0hK5BmC5E1Ud-dfnXq2JdxvRnjHx7zuxC2H5747a0MAlKNZG"
                + "d5lVw9nlv_v0hDrmKhD5QlEAuxukmMEWbjihQsncQz9f9fw00xe6J8OUe_DwfzSW9_htk6-7rERHqi1NtNfRSj_6sajZm4E5jg");
    assertThat(response.getRefreshToken()).isNull();
  }

  private String givenValidRefreshToken() {
    CreateTokenRequest request =
        CreateTokenRequest.builder().username("admin").password("pwd").build();
    AccessTokenResponse response = service.create(request);
    return response.getRefreshToken();
  }
}
