package uk.co.mruoc.nac.usecases;

import java.util.Collection;

public class MultiplePlayersCannotUseTheSameTokenException extends RuntimeException {

  public MultiplePlayersCannotUseTheSameTokenException(Collection<Character> tokens) {
    super(toMessage(tokens));
  }

  private static String toMessage(Collection<Character> tokens) {
    return String.format("multiple players cannot use the same token %s", format(tokens));
  }

  private static String format(Collection<Character> tokens) {
    return String.join(",", toStrings(tokens));
  }

  private static Collection<String> toStrings(Collection<Character> chars) {
    return chars.stream().map(Object::toString).toList();
  }
}
