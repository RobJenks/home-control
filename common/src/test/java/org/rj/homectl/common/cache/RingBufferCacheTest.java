package org.rj.homectl.common.cache;

import org.jooq.lambda.Seq;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RingBufferCacheTest {

    @Test
    public void testCountWithAppend() {
        final var cache = new RingBufferCache<Integer>(100);

        final var expectedCount =
                Seq.range(0, 100).concat(           // 0 - 100
                        Seq.of(100).cycle(150));    // 100 (*150)

        Seq.range(0, 250).zip(expectedCount)
                .forEach(x -> {
                    Assert.assertEquals("Invalid count", x.v2.intValue(), cache.getCount());
                    cache.add(x.v1);
                });
    }

    @Test
    public void testBasicAppend() {
        final var cache = new RingBufferCache<Integer>(10);
        Assert.assertTrue(cache.isEmpty());
        Assert.assertFalse(cache.isFull());

        Seq.rangeClosed(1, 8).forEach(cache::add);
        Assert.assertEquals(Seq.rangeClosed(1, 8).toList(), cache.toList());
        Assert.assertEquals(8, cache.getCount());
        Assert.assertEquals(10, cache.getCapacity());
        Assert.assertFalse(cache.isEmpty());
        Assert.assertFalse(cache.isFull());

        Seq.rangeClosed(9, 12).forEach(cache::add);
        Assert.assertEquals(List.of(3, 4, 5, 6, 7, 8, 9, 10, 11, 12), cache.toList());
        Assert.assertEquals(10, cache.getCount());
        Assert.assertEquals(10, cache.getCapacity());
        Assert.assertTrue(cache.isFull());
        Assert.assertFalse(cache.isEmpty());
    }

    @Test
    public void testRemoval() {
        final var cache = new RingBufferCache<Integer>(10);

        Seq.rangeClosed(1, 5).forEach(cache::add);
        Assert.assertEquals(Seq.rangeClosed(1, 5).toList(), cache.toList());    // [1,2,3,4,5]

        cache.removeNewest().ifPresentOrElse(x -> Assert.assertEquals(5, x.intValue()), Assert::fail);
        Assert.assertEquals(Seq.rangeClosed(1, 4).toList(), cache.toList());    // [1,2,3,4]
        Assert.assertEquals(4, cache.getCount());

        cache.removeOldest().ifPresentOrElse(x -> Assert.assertEquals(1, x.intValue()), Assert::fail);
        Assert.assertEquals(Seq.rangeClosed(2, 4).toList(), cache.toList());    // [2,3,4]
        Assert.assertEquals(3, cache.getCount());

        Seq.rangeClosed(2, 4).forEach(x -> {
            Assert.assertFalse(cache.isEmpty());
            Assert.assertEquals(x, cache.removeOldest().orElseThrow());
        });

        Assert.assertTrue(cache.isEmpty());
    }

    @Test
    public void testRemovalAcrossCapacityBoundary() {
        final var cache = new RingBufferCache<Integer>(10);

        Seq.rangeClosed(1, 12).forEach(cache::add);
        Assert.assertEquals(List.of(3, 4, 5, 6, 7, 8, 9, 10, 11, 12), cache.toList()); // buffer: [11, [E]12, [S]3, 4, 5, 6, 7, 8, 9, 10]
        Assert.assertEquals(2, cache.getStart());
        Assert.assertEquals(1, cache.getEnd());
        Assert.assertEquals(10, cache.getCount());

        cache.removeNewest().ifPresentOrElse(x -> Assert.assertEquals(12, x.intValue()), Assert::fail);
        Assert.assertEquals(List.of(3, 4, 5, 6, 7, 8, 9, 10, 11), cache.toList()); // buffer: [[E]11, 12, [S]3, 4, 5, 6, 7, 8, 9, 10]
        Assert.assertEquals(2, cache.getStart());
        Assert.assertEquals(0, cache.getEnd());
        Assert.assertEquals(9, cache.getCount());

        cache.removeNewest().ifPresentOrElse(x -> Assert.assertEquals(11, x.intValue()), Assert::fail);
        Assert.assertEquals(List.of(3, 4, 5, 6, 7, 8, 9, 10), cache.toList()); // buffer: [11, 12, [S]3, 4, 5, 6, 7, 8, 9, [E]10]
        Assert.assertEquals(2, cache.getStart());
        Assert.assertEquals(10, cache.getEnd());
        Assert.assertEquals(8, cache.getCount());
    }

    @Test
    public void testBasicIteratorRetrieval() {
        testIteratorRetrieval(10, Seq.rangeClosed(1, 8).toList(), Seq.rangeClosed(1, 8).toList());
    }

    @Test
    public void testWrappingIteratorRetrieval() {
        testIteratorRetrieval(10, Seq.rangeClosed(1, 14).toList(), List.of(5,6,7,8,9,10,11,12,13,14));
    }

    @Test
    public void testTwiceWrappingIteratorRetrieval() {
        testIteratorRetrieval(10, Seq.rangeClosed(1, 28).toList(), List.of(19,20,21,22,23,24,25,26,27,28));
    }

    private void testIteratorRetrieval(int cacheCapacity, List<Integer> itemsToAdd, List<Integer> expected) {
        final var cache = new RingBufferCache<Integer>(cacheCapacity);
        itemsToAdd.forEach(cache::add);

        Assert.assertEquals(expected, cache.toList());

        final var iteratorOutput = new ArrayList<>();
        for (final var item : cache) {
            iteratorOutput.add(item);
        }

        Assert.assertEquals(expected, iteratorOutput);
    }

    @Test
    public void testBasicStreamRetrieval() {
        testStreamRetrieval(10, Seq.rangeClosed(1, 8).toList(), Seq.rangeClosed(1, 8).toList());
    }

    @Test
    public void testWrappingStreamRetrieval() {
        testStreamRetrieval(10, Seq.rangeClosed(1, 14).toList(), List.of(5,6,7,8,9,10,11,12,13,14));
    }

    @Test
    public void testTwiceWrappingStreamRetrieval() {
        testStreamRetrieval(10, Seq.rangeClosed(1, 28).toList(), List.of(19,20,21,22,23,24,25,26,27,28));
    }

    private void testStreamRetrieval(int cacheCapacity, List<Integer> itemsToAdd, List<Integer> expected) {
        final var cache = new RingBufferCache<Integer>(cacheCapacity);
        itemsToAdd.forEach(cache::add);

        Assert.assertEquals(expected, cache.toList());

        final var output = cache.stream().collect(Collectors.toList());
        Assert.assertEquals(expected, output);
    }

    @Test
    public void testClearCache() {
        final var cache = new RingBufferCache<Integer>(10);
        Assert.assertTrue(cache.isEmpty());

        Seq.rangeClosed(1, 32).forEach(cache::add);
        Assert.assertTrue(cache.isFull());
        Assert.assertFalse(cache.isEmpty());

        cache.clear();
        Assert.assertTrue(cache.isEmpty());
        Assert.assertFalse(cache.isFull());
    }

}
