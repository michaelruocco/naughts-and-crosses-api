package uk.co.mruoc.nac.client;

import uk.co.mruoc.nac.api.dto.ApiGame;

public interface GameUpdateListener {

  void updated(ApiGame game);
}
