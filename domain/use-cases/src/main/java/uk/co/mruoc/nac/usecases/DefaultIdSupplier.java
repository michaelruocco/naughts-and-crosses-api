package uk.co.mruoc.nac.usecases;

import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DefaultIdSupplier implements IdSupplier {

    private final long initialValue;
    private final AtomicLong nextId;

    public DefaultIdSupplier() {
        this(1);
    }

    public DefaultIdSupplier(long initialValue) {
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
