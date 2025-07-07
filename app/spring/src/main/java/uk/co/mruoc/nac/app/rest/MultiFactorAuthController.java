package uk.co.mruoc.nac.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.api.converter.ApiMfaConverter;
import uk.co.mruoc.nac.api.dto.ApiAssociateSoftwareTokenResponse;
import uk.co.mruoc.nac.api.dto.ApiMfaSettings;
import uk.co.mruoc.nac.api.dto.ApiVerifySoftwareTokenRequest;
import uk.co.mruoc.nac.entities.MfaPreferences;
import uk.co.mruoc.nac.entities.VerifySoftwareTokenRequest;
import uk.co.mruoc.nac.usecases.MultiFactorAuthService;

@RestController
@RequiredArgsConstructor
public class MultiFactorAuthController {

  private final ApiMfaConverter converter;
  private final MultiFactorAuthService service;

  @PostMapping("/v1/auth/software-tokens")
  public ApiAssociateSoftwareTokenResponse create() {
    String secretCode = service.createSoftwareToken();
    return new ApiAssociateSoftwareTokenResponse(secretCode);
  }

  @PutMapping("/v1/auth/software-tokens")
  public void verify(@RequestBody ApiVerifySoftwareTokenRequest apiRequest) {
    VerifySoftwareTokenRequest request = converter.toRequest(apiRequest);
    service.verifySoftwareToken(request);
  }

  @PutMapping("/v1/users/{username}/mfa-settings")
  public ApiMfaSettings updatePreferences(
      @PathVariable String username, @RequestBody ApiMfaSettings apiSettings) {
    MfaPreferences preferences = converter.toPreferences(username, apiSettings);
    service.updatePreferences(preferences);
    return getSettings(username);
  }

  @GetMapping("/v1/users/{username}/mfa-settings")
  public ApiMfaSettings getSettings(@PathVariable String username) {
    return converter.toApiSettings(service.getSettings(username));
  }
}
