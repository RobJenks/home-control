package org.rj.homectl.common.cache;

import java.util.Iterator;

public class RingBufferCacheIterators {

    public static class ForwardIterator<T> implements Iterator<T> {
        private final RingBufferCache<T> cache;
        private int index;

        ForwardIterator(RingBufferCache<T> cache) {
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

    public static class ReverseIterator<T> implements Iterator<T> {
        private final RingBufferCache<T> cache;
        private int index;

        ReverseIterator(RingBufferCache<T> cache) {
            this.cache = cache;
            this.index = cache.getEnd();
        }

        @Override
        public boolean hasNext() {
            return (index != cache.getStart());
        }

        @Override
        public T next() {
            index = cache.prevIndex(index);
            return cache.bufferItem(index);
        }
    }
}