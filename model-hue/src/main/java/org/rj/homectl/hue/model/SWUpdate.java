package org.rj.homectl.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class SWUpdate {
    @JsonProperty("lastinstall")
    private LocalDate lastInstall;
    private String state;

    public SWUpdate() { }

    public LocalDate getLastInstall() {
        return lastInstall;
    }

    public void setLastInstall(LocalDate lastInstall) {
        this.lastInstall = lastInstall;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
