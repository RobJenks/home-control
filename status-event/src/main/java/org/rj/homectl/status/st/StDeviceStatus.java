package org.rj.homectl.status.st;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.rj.homectl.st.model.DeviceStatus;

public class StDeviceStatus {
    private String id;
    private DeviceStatus status;

    public StDeviceStatus() { }

    public StDeviceStatus(String id, DeviceStatus status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DeviceStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StDeviceStatus that = (StDeviceStatus) o;

        return new EqualsBuilder().append(id, that.id).append(status, that.status).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(status).toHashCode();
    }
}
