package org.rj.homectl.kafka.producer;

import org.apache.kafka.clients.producer.Producer;

import java.util.Map;
import java.util.function.Function;

/**
 * Generator which builds a new producer given a set of fully-prepared producer config
 */
public interface ProducerGenerator<K, V> extends Function<Map<String, Object>, Producer<K, V>> { }
