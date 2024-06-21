package uk.co.mruoc.nac.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GenericCollector<S, M> implements Collector<S, Collection<S>, M> {

  private final Function<Collection<S>, M> finisher;

  @Override
  public Supplier<Collection<S>> supplier() {
    return ArrayList::new;
  }

  @Override
  public BiConsumer<Collection<S>, S> accumulator() {
    return Collection::add;
  }

  @Override
  public BinaryOperator<Collection<S>> combiner() {
    return ((p1, p2) -> Stream.concat(p1.stream(), p2.stream()).toList());
  }

  @Override
  public Function<Collection<S>, M> finisher() {
    return finisher;
  }

  @Override
  public Set<Characteristics> characteristics() {
    return Collections.emptySet();
  }
}
