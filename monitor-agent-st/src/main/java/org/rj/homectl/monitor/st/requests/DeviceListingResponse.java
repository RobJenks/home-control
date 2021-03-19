package org.rj.homectl.monitor.st.requests;

import org.rj.homectl.st.model.Device;

import java.util.List;

public class DeviceListingResponse {
    private List<Device> items;

    public DeviceListingResponse() { }

    public List<Device> getItems() {
        return items;
    }

    public void setItems(List<Device> items) {
        this.items = items;
    }
}
