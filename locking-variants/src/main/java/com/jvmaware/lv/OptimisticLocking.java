package com.jvmaware.lv;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Optimistic locking is a way of managing concurrency control, generally used by transactional systems.
 * In this implementation, we allow multiple threads to compete for the update completion but only committing
 * the transaction if it’s not already updated.
 * <p>
 * The same is implemented using {@link AtomicInteger} in this example.
 *
 * @author gaurs
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(value = 2, warmups = 1)
@Warmup(iterations = 5)
@Measurement(iterations = 5)
@Threads(value = Threads.MAX)
public class OptimisticLocking {

    @State(Scope.Benchmark)
    public static class CustomState {
        private final AtomicInteger sharedResource = new AtomicInteger();

        public void inc() {
            sharedResource.getAndIncrement();
        }

        public AtomicInteger getSharedResource() {
            return sharedResource;
        }
    }

    @Benchmark
    public void increment(CustomState state, Blackhole blackhole) {
        state.inc();
        blackhole.consume(state.getSharedResource());
    }
}
