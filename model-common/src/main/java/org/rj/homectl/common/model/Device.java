package org.rj.homectl.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

@JsonDeserialize(converter = Device.PostDeserializationSanitizer.class)
public class Device {
    private String id;
    private String name;
    @JsonProperty("class")
    private DeviceClass deviceClass;
    private String room;
    private Schematic schematic;
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

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Schematic getSchematic() {
        return schematic;
    }

    public void setSchematic(Schematic schematic) {
        this.schematic = schematic;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }


    protected static class PostDeserializationSanitizer extends StdConverter<Device, Device> {
        @Override
        public Device convert(Device device) {
            // Validation
            if (device.id == null) throw new RuntimeException("Device id cannot be null");

            // Default values where necessary
            if (device.name == null) device.name = device.id;

            return device;
        }
    }
}
