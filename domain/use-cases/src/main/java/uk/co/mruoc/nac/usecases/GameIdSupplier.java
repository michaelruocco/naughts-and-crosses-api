package uk.co.mruoc.nac.usecases;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.LongSupplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GameIdSupplier implements LongSupplier {

    private final AtomicLong atomicLong;

    public GameIdSupplier() {
        this(new AtomicLong(1));
    }

    @Override
    public long getAsLong() {
        return atomicLong.getAndIncrement();
    }
}
