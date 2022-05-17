package org.rj.homectl.kafka.metrics;

import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.jooq.lambda.tuple.Tuple2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KafkaMetricsCollector {
    private static final Logger log = LoggerFactory.getLogger(KafkaMetricsCollector.class);

    public static Map<String, ? extends Metric> collectMetrics(Map<MetricName, ? extends Metric> metricSet, Set<String> desiredMetrics) {
        return metricSet.entrySet().stream()
                .filter(x -> desiredMetrics.stream().anyMatch(x.getKey().name()::equals))
                .collect(Collectors.toMap(x -> x.getKey().name(), Map.Entry::getValue));
    }

    public static Map<String, Double> collectNumericMetrics(Map<MetricName, ? extends Metric> metricSet, Set<String> desiredMetrics) {
        try {
            return metricSet.entrySet().stream()
                    .filter(x -> desiredMetrics.stream().anyMatch(x.getKey().name()::equals))
                    .filter(x -> {
                        final var validType = x.getValue().metricValue() instanceof Double;
                        if (!validType) log.error("Cannot collect metric '{}'; metric is not a valid numeric type", x.getKey().name());
                        return validType;
                    })
                    // Note: de-duplicate metric names where certain metrics are returned for both "overall" and for a specific topic.
                    // We are only monitoring a specific topic in this case so can consider them equal and just discard one
                    .collect(Collectors.toMap(x -> x.getKey().name(), x -> (Double) x.getValue().metricValue(), (x1, x2) -> x2));
        } catch (Throwable t) {
            log.error("Failed to collect aggregation metrics ({})", t.getMessage(), t);
            return Map.of();
        }
    }

    public static void consumeNumericMetrics(Map<MetricName, ? extends Metric> metricSet, Set<Tuple2<String, Consumer<Double>>> desiredMetrics) {
        metricSet.entrySet().forEach(entry -> {
            final var findResult = desiredMetrics.stream().filter(x -> entry.getKey().name().equals(x.v1)).findAny();
            findResult.ifPresent(consumer -> {
                final var valueObject = entry.getValue().metricValue();
                if (!(valueObject instanceof Double)) {
                    log.error("Cannot collect metric '{}'; metric is not a valid numeric type", entry.getKey().name());
                }
                else {
                    consumer.v2.accept((Double) entry.getValue().metricValue());
                }
            });
        });
    }

}
