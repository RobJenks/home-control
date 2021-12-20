package org.rj.homectl.common.util;

import java.util.List;
import java.util.function.Consumer;

public class BackoffDelay {
    private final List<Long> sequence;
    private final Consumer<Long> onDelay;
    private final Consumer<InterruptedException> onInterrupt;
    private int currentDelayIx;

    public BackoffDelay(List<Long> delaySecs, Consumer<Long> onDelay, Consumer<InterruptedException> onInterrupt) {
        if (delaySecs.size() < 1) throw new IllegalArgumentException("Cannot construct backoff delay without valid sequence");

        this.sequence = delaySecs;
        this.onDelay = onDelay;
        this.onInterrupt = onInterrupt;
        this.currentDelayIx = 0;
    }

    public void delay() {
        final var currentDelay = sequence.get(currentDelayIx);
        onDelay.accept(currentDelay);

        Util.threadSleepOrElse(currentDelay * 1000L, onInterrupt);
        if (currentDelayIx < (sequence.size() - 1)) currentDelayIx++;
    }

    public void reset() {
        currentDelayIx = 0;
    }
}
