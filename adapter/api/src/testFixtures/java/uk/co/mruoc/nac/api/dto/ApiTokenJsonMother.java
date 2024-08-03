package uk.co.mruoc.nac.api.dto;

import static uk.co.mruoc.file.FileLoader.loadContentFromClasspath;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiTokenJsonMother {

  public static String refreshResponse() {
    return loadContentFromClasspath("token/refresh-response.json");
  }

  public static String authCodeResponse() {
    return loadContentFromClasspath("token/auth-code-response.json");
  }
}
