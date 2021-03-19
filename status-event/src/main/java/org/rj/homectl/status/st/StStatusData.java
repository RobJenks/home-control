package org.rj.homectl.status.st;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.rj.homectl.st.model.DeviceStatus;
import org.rj.homectl.status.events.StatusEventContent;

import java.util.HashMap;

public class StStatusData implements StatusEventContent {
    private StEventType type;
    private StDeviceListing devices;
    private StDeviceStatuses status;

    public StStatusData() { }

    public StStatusData(StEventType type, StDeviceListing devices, StDeviceStatuses status) {
        this.type = type;
        this.devices = devices;
        this.status = status;
    }

    public StEventType getType() {
        return type;
    }

    public void setType(StEventType type) {
        this.type = type;
    }

    public StDeviceListing getDevices() {
        return devices;
    }

    public void setDevices(StDeviceListing devices) {
        this.devices = devices;
    }

    public StDeviceStatuses getStatus() {
        return status;
    }

    public void setStatus(StDeviceStatuses status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StStatusData that = (StStatusData) o;

        return new EqualsBuilder().append(type, that.type).append(devices, that.devices)
                .append(status, that.status).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).append(devices)
                .append(status).toHashCode();
    }
}
