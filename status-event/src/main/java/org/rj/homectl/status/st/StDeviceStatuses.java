package org.rj.homectl.status.st;

import org.rj.homectl.st.model.DeviceStatus;

import java.util.ArrayList;
import java.util.Collection;

public class StDeviceStatuses extends ArrayList<StDeviceStatus> {
    public StDeviceStatuses() {
        super();
    }

    public StDeviceStatuses(Collection<? extends StDeviceStatus> c) {
        super(c);
    }
}
