package org.rj.homectl.common.cache;

import org.jooq.lambda.Seq;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class RingBufferCache<T> implements Iterable<T> {
    private final List<T> buffer;
    private final int internalCapacity; // Equal to requested capacity + 1, to allow non-overlapping buffer pointers
    private int start;
    private int end;

    public RingBufferCache(int capacity) {
        this(capacity, () -> null);
    }

    public RingBufferCache(int capacity, Supplier<T> initialValues) {
        this(capacity, __ -> initialValues.get());
    }

    public RingBufferCache(int capacity, Function<Integer, T> initialValues) {
        this.internalCapacity = capacity + 1;
        this.start = 0;
        this.end = 0;

        this.buffer = Seq.range(0, internalCapacity).map(initialValues).collect(Collectors.toList());
    }

    public void add(T item) {
        if (isFull()) {
            buffer.set(end, item);
            start = nextIndex(start);
            end = nextIndex(end);
        }
        else {
            buffer.set(end, item);
            end = nextIndex(end);
        }
    }

    public Optional<T> removeOldest() {
        if (isEmpty()) return Optional.empty();

        T item = buffer.get(start);
        start = nextIndex(start);

        return Optional.ofNullable(item);
    }

    public Optional<T> removeNewest() {
        if (isEmpty()) return Optional.empty();

        end = prevIndex(end);
        T item = buffer.get(end);

        return Optional.ofNullable(item);
    }

    public void clear() {
        start = 0;
        end = 0;
    }

    public int getCapacity() {
        return internalCapacity - 1;
    }

    public int getCount() {
        if (start <= end) {
            return end - start;
        }
        else {
            return (end + internalCapacity) - start;
        }
    }

    public boolean isEmpty() {
        return (start == end);
    }

    public boolean isFull() {
        return (nextIndex(end) == start);
    }

    public Iterator<T> iterator() {
        return new RingBufferCacheIterator<>(this);
    }

    public Stream<T> stream() {
        return StreamSupport.stream(
                Spliterators.spliterator(iterator(), 0L, 0), false);
    }

    public List<T> toList() {
        return toList(Long.MAX_VALUE);
    }

    public List<T> toList(long maxElements) {
        return Seq.iterate(start, this::nextIndex)
                .limitWhile(i -> i != end)
                .limit(maxElements)
                .map(buffer::get)
                .collect(Collectors.toList());
    }

    public List<T> extractInternalBuffer() {
        return new ArrayList<>(buffer);
    }

    public String debugState() {
        return String.format("buffer=%s, capacity=%d, internalCapacity=%d, start=%d, end=%d%s%s",
                buffer, getCapacity(), internalCapacity, start, end,
                isEmpty() ? ", is empty" : "",
                isFull() ? ", is full" : "");
    }

    int getStart() { return start; }
    int getEnd() { return end; }

    int nextIndex(int currentIndex) {
        return (currentIndex + 1) % internalCapacity;
    }

    int prevIndex(int currentIndex) {
        return ((currentIndex - 1) + internalCapacity) % internalCapacity;
    }

    T bufferItem(int bufferIndex) {
        return buffer.get(bufferIndex);
    }

}
