package uk.co.mruoc.nac.repository.inmemory;

import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import uk.co.mruoc.nac.usecases.IdSupplier;

@RequiredArgsConstructor
public class InMemoryIdSupplier implements IdSupplier {

  private final long initialValue;
  private final AtomicLong nextId;

  public InMemoryIdSupplier() {
    this(1);
  }

  public InMemoryIdSupplier(long initialValue) {
    this(initialValue, new AtomicLong(initialValue));
  }

  @Override
  public long getAsLong() {
    return nextId.getAndIncrement();
  }

  @Override
  public void reset() {
    nextId.set(initialValue);
  }
}
