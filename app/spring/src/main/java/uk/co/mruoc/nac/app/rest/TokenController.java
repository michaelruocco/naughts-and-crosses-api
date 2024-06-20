package uk.co.mruoc.nac.app.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.api.converter.ApiTokenConverter;
import uk.co.mruoc.nac.api.dto.ApiCreateTokenRequest;
import uk.co.mruoc.nac.api.dto.ApiCreateTokenResponse;
import uk.co.mruoc.nac.entities.CreateTokenRequest;
import uk.co.mruoc.nac.entities.CreateTokenResponse;
import uk.co.mruoc.nac.usecases.TokenService;

@RestController
@RequestMapping("/v1/tokens")
@RequiredArgsConstructor
public class TokenController {

  private final ApiTokenConverter converter;
  private final TokenService service;

  @PostMapping
  public ApiCreateTokenResponse create(@RequestBody ApiCreateTokenRequest apiRequest) {
    CreateTokenRequest request = converter.toRequest(apiRequest);
    CreateTokenResponse response = service.create(request);
    return converter.toApiResponse(response);
  }
}
