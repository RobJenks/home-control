package org.rj.homectl.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;

import java.util.Map;
import java.util.function.Function;

/**
 * Generator that builds a new consumer given a set of fully-prepared consumer config
 */
public interface ConsumerGenerator<K, V> extends Function<Map<String, Object>, Consumer<K, V>> { }
