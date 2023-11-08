package uk.co.mruoc.nac.usecases;

import java.util.function.LongSupplier;

public interface IdSupplier extends LongSupplier {

  void reset();
}
