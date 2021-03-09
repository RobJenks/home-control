package org.rj.homectl.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class SWUpdate {
    @JsonProperty("lastinstall")
    private OffsetDateTime lastInstall;
    private String state;

    public SWUpdate() { }

    public OffsetDateTime getLastInstall() {
        return lastInstall;
    }

    public void setLastInstall(OffsetDateTime lastInstall) {
        this.lastInstall = lastInstall;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
