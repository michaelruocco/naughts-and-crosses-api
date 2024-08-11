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
import uk.co.mruoc.nac.api.dto.ApiCreateGameRequest;
import uk.co.mruoc.nac.api.dto.ApiGame;
import uk.co.mruoc.nac.api.dto.ApiGamePage;
import uk.co.mruoc.nac.api.dto.ApiGamePageRequest;
import uk.co.mruoc.nac.api.dto.ApiTurn;
import uk.co.mruoc.nac.entities.CreateGameRequest;
import uk.co.mruoc.nac.entities.Game;
import uk.co.mruoc.nac.entities.GamePageRequest;
import uk.co.mruoc.nac.entities.Turn;
import uk.co.mruoc.nac.usecases.GameFacade;

@RestController
@RequestMapping("/v1/games")
@RequiredArgsConstructor
public class GameController {

  private final GameFacade facade;
  private final ApiConverter converter;

  @GetMapping
  public Collection<ApiGame> getAll(
      @RequestParam(name = "minimal", required = false, defaultValue = "false") boolean minimal) {
    return facade.getAll().map(toGameConverter(minimal)).toList();
  }

  @PostMapping("/pages")
  public ApiGamePage createPage(@RequestBody ApiGamePageRequest apiRequest) {
    GamePageRequest request = converter.toRequest(apiRequest);
    var page = facade.createPage(request);
    return converter.toApiGamePage(page, toGameConverter(apiRequest.getMinimal()));
  }

  @DeleteMapping
  public void deleteAll() {
    facade.deleteAll();
  }

  @GetMapping("/{id}")
  public ApiGame get(
      @PathVariable long id,
      @RequestParam(name = "minimal", required = false, defaultValue = "false") boolean minimal) {
    return toGameConverter(minimal).apply(facade.get(id));
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) {
    facade.delete(id);
  }

  @PostMapping
  public ApiGame create(@RequestBody ApiCreateGameRequest apiRequest) {
    CreateGameRequest request = converter.toCreateGameRequest(apiRequest);
    Game game = facade.createGame(request);
    return converter.toApiGame(game);
  }

  @PostMapping("/{gameId}/turns")
  public ApiGame takeTurn(@PathVariable long gameId, @RequestBody ApiTurn apiTurn) {
    Turn turn = converter.toTurn(apiTurn);
    Game game = facade.takeTurn(gameId, turn);
    return converter.toApiGame(game);
  }

  private Function<Game, ApiGame> toGameConverter(boolean minimal) {
    if (minimal) {
      return converter::toMinimalApiGame;
    }
    return converter::toApiGame;
  }
}
