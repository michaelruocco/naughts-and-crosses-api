package uk.co.mruoc.nac.entities;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Builder(toBuilder = true)
@Data
public class UserName {

  private final String full;
  private final String first;
  private final String last;

  public UserName tryToPopulateAll() {
    return tryPopulateAll(this);
  }

  public boolean hasFirstAndLastName() {
    return hasFirstName() && hasLastName();
  }

  public boolean hasFirstName() {
    return StringUtils.isNotEmpty(first);
  }

  public boolean hasLastName() {
    return StringUtils.isNotEmpty(last);
  }

  public boolean hasFullName() {
    return StringUtils.isNotEmpty(full);
  }

  public static UserName tryPopulateAll(UserName name) {
    if (name.hasFirstAndLastName()) {
      return toUserName(name.getFirst(), name.getLast());
    }
    if (name.hasFullName()) {
      return toUserName(name.getFull());
    }
    if (name.hasFirstName()) {
      return firstNameToUserName(name.getFirst());
    }
    if (name.hasLastName()) {
      return lastNameToUserName(name.getLast());
    }
    return name;
  }

  public static UserName toUserName(String first, String last) {
    return UserName.builder().full(toFull(first, last)).first(first).last(last).build();
  }

  private static String toFull(String first, String last) {
    return Stream.of(first, last).filter(StringUtils::isNotEmpty).collect(Collectors.joining(" "));
  }

  public static UserName toUserName(String full) {
    List<String> parts = splitName(full);
    if (parts.size() == 1) {
      return handleSingleName(parts);
    }
    return handleMultipleNames(full, parts);
  }

  private static List<String> splitName(String full) {
    if (StringUtils.isEmpty(full)) {
      return Collections.emptyList();
    }
    return List.of(full.split(" "));
  }

  private static UserName handleSingleName(List<String> parts) {
    String name = parts.get(0);
    return UserName.builder().full(name).last(name).build();
  }

  private static UserName handleMultipleNames(String full, List<String> parts) {
    return UserName.builder()
        .full(full)
        .first(toFirst(parts).orElse(null))
        .last(toLast(parts).orElse(null))
        .build();
  }

  private static Optional<String> toFirst(List<String> parts) {
    if (parts.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(parts.get(0));
  }

  private static Optional<String> toLast(List<String> parts) {
    if (parts.size() > 1) {
      return Optional.of(String.join(" ", parts.subList(1, parts.size())));
    }
    return Optional.empty();
  }

  private static UserName firstNameToUserName(String first) {
    return UserName.builder().first(first).full(first).build();
  }

  private static UserName lastNameToUserName(String last) {
    return UserName.builder().last(last).full(last).build();
  }
}
