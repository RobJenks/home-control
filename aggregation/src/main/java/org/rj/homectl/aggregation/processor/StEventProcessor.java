package org.rj.homectl.aggregation.processor;

import org.rj.homectl.aggregation.state.AggregateState;
import org.rj.homectl.common.model.DeviceClass;
import org.rj.homectl.st.model.state.StDeviceState;
import org.rj.homectl.status.st.StDeviceListing;
import org.rj.homectl.status.st.StDeviceStatuses;
import org.rj.homectl.status.st.StEventType;
import org.rj.homectl.status.st.StStatusEvent;

import static org.jooq.lambda.tuple.Tuple.*;

public class StEventProcessor extends AbstractEventProcessor<StStatusEvent> {
    public StEventProcessor(AggregateState state) {
        super(state);
    }

    @Override
    public void processEvent(StStatusEvent event) {
        final var eventType = event.getData().getType();
        if (eventType == StEventType.Snapshot) {
            processSnapshotEvent(event.getData().getDevices());
        }
        else if (eventType == StEventType.Events) {
            processStatusEvents(event.getData().getStatus());
        }
    }

    public void processSnapshotEvent(StDeviceListing listing) {
        listing.stream()
                .map(x -> tuple(x, getState().getDeviceState(x.getDeviceId(), DeviceClass.St, StDeviceState::new)))
                .filter(x -> x.v2.isPresent())
                .forEach(x -> x.v2.get().updateFromListing(x.v1));
    }

    public void processStatusEvents(StDeviceStatuses status) {
        status.stream()
                .map(x -> tuple(x, getState().getDeviceState(x.getId(), DeviceClass.St, StDeviceState::new)))
                .filter(x -> x.v2.isPresent())
                .forEach(x -> x.v2.get().updateFromStatus(x.v1.getStatus()));
    }
}
