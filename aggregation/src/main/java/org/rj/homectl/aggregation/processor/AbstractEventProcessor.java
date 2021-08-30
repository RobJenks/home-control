package org.rj.homectl.aggregation.processor;

import org.rj.homectl.aggregation.state.AggregateState;
import org.rj.homectl.status.events.StatusEvent;

public abstract class AbstractEventProcessor<T extends StatusEvent> {
    private final AggregateState state;

    public AbstractEventProcessor(final AggregateState state) {
        this.state = state;
    }

    public abstract void processEvent(T event);

    protected AggregateState getState() {
        return state;
    }
}
