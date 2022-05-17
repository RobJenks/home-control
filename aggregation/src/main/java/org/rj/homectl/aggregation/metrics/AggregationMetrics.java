package org.rj.homectl.aggregation.metrics;

public class AggregationMetrics {
    private AggregationConsumerMetrics consumerMetrics;

    public AggregationMetrics() { }

    public AggregationMetrics(AggregationConsumerMetrics consumerMetrics) {
        this.consumerMetrics = consumerMetrics;
    }

    public AggregationConsumerMetrics getConsumerMetrics() {
        return consumerMetrics;
    }

    public void setConsumerMetrics(AggregationConsumerMetrics consumerMetrics) {
        this.consumerMetrics = consumerMetrics;
    }
}
