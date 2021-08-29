package org.rj.homectl.aggregation.state;

import org.rj.homectl.common.model.Device;
import org.rj.homectl.common.model.DeviceClass;
import org.rj.homectl.common.model.HomeState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class AggregateState {
    private static final Logger LOG = LoggerFactory.getLogger(AggregateState.class);
    private final HomeState state;

    public AggregateState(HomeState state) {
        this.state = state;
    }

    public HomeState getData() {
        return state;
    }

    public Optional<Device> getDevice(String id) {
        return Optional.ofNullable(id)
                .map(x -> state.getIndexedDevices().get(x));
    }

    public Optional<Device> getDevice(String id, DeviceClass requiredClass) {
        return getDevice(id)
                .map(device -> {
                    if (device.getDeviceClass() != requiredClass) {
                        LOG.warn("Located device '{}' but it has unexpected class '{}'; expecting '{}' device", id, device.getDeviceClass(), requiredClass);
                        return null;
                    }
                    else return device;
                });
    }
}
