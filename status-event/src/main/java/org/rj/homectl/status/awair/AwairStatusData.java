package org.rj.homectl.status.awair;

import org.rj.homectl.awair.model.status.AwairStatus;
import org.rj.homectl.status.events.StatusEventContent;

public class AwairStatusData extends AwairStatus
                             implements StatusEventContent {

    private String deviceId;

    public AwairStatusData() { }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
