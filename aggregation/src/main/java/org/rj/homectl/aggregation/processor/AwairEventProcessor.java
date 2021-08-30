package org.rj.homectl.aggregation.processor;

import org.rj.homectl.aggregation.state.AggregateState;
import org.rj.homectl.awair.model.device.AwairDeviceState;
import org.rj.homectl.common.model.DeviceClass;
import org.rj.homectl.status.awair.AwairStatusEvent;

public class AwairEventProcessor extends AbstractEventProcessor<AwairStatusEvent> {

    public AwairEventProcessor(final AggregateState state) {
        super(state);
    }

    @Override
    public void processEvent(AwairStatusEvent event) {
        final var deviceState = getState().getDeviceState(event.getData().getDeviceId(),
                DeviceClass.Awair, AwairDeviceState::new);

        deviceState.ifPresent(currentState -> currentState.updateFromStatus(event.getData()));
    }
}
