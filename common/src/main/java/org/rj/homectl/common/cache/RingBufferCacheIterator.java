package org.rj.homectl.common.cache;

import java.util.Iterator;

public class RingBufferCacheIterator<T> implements Iterator<T> {
    private final RingBufferCache<T> cache;
    private int index;

    RingBufferCacheIterator(RingBufferCache<T> cache) {
        this.cache = cache;
        this.index = cache.getStart();
    }

    @Override
    public boolean hasNext() {
        return (index != cache.getEnd());
    }

    @Override
    public T next() {
        final T item = cache.bufferItem(index);
        index = cache.nextIndex(index);

        return item;
    }
}
