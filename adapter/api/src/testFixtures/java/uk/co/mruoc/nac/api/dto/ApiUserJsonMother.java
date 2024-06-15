package uk.co.mruoc.nac.api.dto;

import static uk.co.mruoc.file.FileLoader.loadContentFromClasspath;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiUserJsonMother {

  public static String joeBloggs() {
    return loadContentFromClasspath("user/joe-bloggs.json");
  }

  public static String testUserUpdated() {
    return loadContentFromClasspath("user/test-user-updated.json");
  }

  public static String user1() {
    return loadContentFromClasspath("user/user-1.json");
  }
}
