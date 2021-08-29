package org.rj.homectl.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Device {
    private String id;
    private String name;
    @JsonProperty("class")
    private DeviceClass deviceClass;
    private DeviceState state;

    public Device() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public DeviceClass getDeviceClass() {
        return deviceClass;
    }

    public void setDeviceClass(DeviceClass deviceClass) {
        this.deviceClass = deviceClass;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }
}
