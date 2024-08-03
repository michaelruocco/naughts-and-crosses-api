package uk.co.mruoc.nac.app.rest;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.api.converter.ApiUserConverter;
import uk.co.mruoc.nac.api.dto.ApiCandidateGamePlayer;
import uk.co.mruoc.nac.usecases.UserService;

@RestController
@RequestMapping("/v1/games/candidate-players")
@RequiredArgsConstructor
public class CandidateGamePlayersController {

  private final UserService service;
  private final ApiUserConverter converter;

  @GetMapping
  public Collection<ApiCandidateGamePlayer> getAll() {
    return service.getAll().map(converter::toApiCandidateGamePlayer).toList();
  }
}
