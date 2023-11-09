package uk.co.mruoc.nac.app.rest;

import java.util.Collection;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uk.co.mruoc.nac.api.converter.ApiConverter;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.Turn;
import uk.co.mruoc.nac.usecases.GameService;

@RestController
@RequestMapping("/v1/games")
@RequiredArgsConstructor
public class GameController {

  private final GameService service;
  private final ApiConverter converter;

  @GetMapping
  public Collection<ApiGame> getAllGames(
      @RequestParam(name = "minimal", required = false, defaultValue = "false") boolean minimal) {
    return service.getAll().map(toGameConverter(minimal)).toList();
  }

  @DeleteMapping
  public void deleteAllGames() {
    service.deleteAll();
  }

  @GetMapping("/{id}")
  public ApiGame getGame(
      @PathVariable long id,
      @RequestParam(name = "minimal", required = false, defaultValue = "false") boolean minimal) {
    return toGameConverter(minimal).apply(service.get(id));
  }

  @PostMapping
  public ApiGame createGame() {
    Game game = service.createGame();
    return converter.toApiGame(game);
  }

  @PostMapping("/{gameId}/turns")
  public ApiGame takeTurn(@PathVariable long gameId, @RequestBody ApiTurn apiTurn) {
    Turn turn = converter.toTurn(apiTurn);
    Game game = service.takeTurn(gameId, turn);
    return converter.toApiGame(game);
  }

  private Function<Game, ApiGame> toGameConverter(boolean minimal) {
    if (minimal) {
      return converter::toMinimalApiGame;
    }
    return converter::toApiGame;
  }
}
