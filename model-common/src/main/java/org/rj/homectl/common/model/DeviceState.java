package org.rj.homectl.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

public abstract class DeviceState {
    private OffsetDateTime lastUpdate;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public abstract Object getFullState();

    public OffsetDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void recordUpdate() {
        this.lastUpdate = OffsetDateTime.now();
    }

    // Can be overridden by subclasses if they have a better way of reporting the update timestamp
    protected void setLastUpdate(OffsetDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

}
