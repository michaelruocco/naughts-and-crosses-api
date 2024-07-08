package uk.co.mruoc.nac.app.rest;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.entities.User;
import uk.co.mruoc.nac.usecases.UserService;

@RestController
@RequestMapping("/v1/games/candidate-players/usernames")
@RequiredArgsConstructor
public class CandidateGamePlayersController {

  private final UserService service;

  @GetMapping
  public Collection<String> getAll() {
    return service.getAll().map(User::getUsername).toList();
  }
}
