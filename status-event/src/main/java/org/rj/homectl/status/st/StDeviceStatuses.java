package org.rj.homectl.status.st;

import org.rj.homectl.st.model.DeviceStatus;

import java.util.ArrayList;
import java.util.Collection;

public class StDeviceStatuses extends ArrayList<DeviceStatus> {
    public StDeviceStatuses() {
        super();
    }

    public StDeviceStatuses(Collection<? extends DeviceStatus> c) {
        super(c);
    }
}
