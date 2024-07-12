package uk.co.mruoc.nac.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.api.converter.ApiTokenConverter;
import uk.co.mruoc.nac.api.dto.ApiCreateTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiRefreshTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiTokenResponse;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.RefreshTokenRequest;
import uk.co.mruoc.nac.entities.TokenResponse;
import uk.co.mruoc.nac.usecases.TokenService;

@RestController
@RequestMapping("/v1/tokens")
@RequiredArgsConstructor
public class TokenController {

  private final ApiTokenConverter converter;
  private final TokenService service;

  @PostMapping
  public ApiTokenResponse create(@RequestBody ApiCreateTokenRequest apiRequest) {
    CreateTokenRequest request = converter.toRequest(apiRequest);
    TokenResponse response = service.create(request);
    return converter.toApiResponse(response);
  }

  @PutMapping
  public ApiTokenResponse create(@RequestBody ApiRefreshTokenRequest apiRequest) {
    RefreshTokenRequest request = converter.toRequest(apiRequest);
    TokenResponse response = service.refresh(request);
    return converter.toApiResponse(response);
  }
}
