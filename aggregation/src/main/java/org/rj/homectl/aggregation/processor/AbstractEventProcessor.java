package org.rj.homectl.aggregation.processor;

import org.rj.homectl.aggregation.state.AggregateState;
import org.rj.homectl.metrics.MetricsExporter;
import org.rj.homectl.status.events.StatusEvent;

public abstract class AbstractEventProcessor<T extends StatusEvent> {
    private final AggregateState state;
    private final MetricsExporter metricsExporter;

    public AbstractEventProcessor(final AggregateState state, final MetricsExporter metricsExporter) {
        this.state = state;
        this.metricsExporter = metricsExporter;
    }

    public abstract void processEvent(T event);

    protected AggregateState getState() {
        return state;
    }

    protected MetricsExporter getMetricsExporter() {
        return metricsExporter;
    }
}
