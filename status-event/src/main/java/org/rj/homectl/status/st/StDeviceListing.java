package org.rj.homectl.status.st;

import org.rj.homectl.st.model.Device;

import java.util.ArrayList;
import java.util.Collection;

public class StDeviceListing extends ArrayList<Device> {
    public StDeviceListing() {
        super();
    }

    public StDeviceListing(Collection<? extends Device> c) {
        super(c);
    }
}
