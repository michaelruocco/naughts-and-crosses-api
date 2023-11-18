package uk.co.mruoc.nac.client;

import io.micrometer.common.util.StringUtils;
import java.util.Scanner;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.api.dto.ApiGame;

@RequiredArgsConstructor
public class NaughtsAndCrossesWebsocketClientMain {
  private static final String URL = "http://localhost:3002";

  public static void main(String[] args) {
    DefaultGameUpdateListener listener = new DefaultGameUpdateListener();
    try (NaughtsAndCrossesWebsocketClient client = new NaughtsAndCrossesWebsocketClient(URL)) {
      client.add(listener);
      client.connect();
      System.out.println("connection completed, waiting for user input to close");
      boolean running = true;
      Scanner scanner = new Scanner(System.in);
      while (running) {
        System.out.println("updates " + listener.getAllUpdates().map(ApiGame::getId).toList());
        listener.reset();
        running = StringUtils.isEmpty(scanner.nextLine());
      }
    }
  }
}
