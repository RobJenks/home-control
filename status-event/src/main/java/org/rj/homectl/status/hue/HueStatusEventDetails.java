package org.rj.homectl.status.hue;

import java.util.Map;

public class HueStatusEventDetails {
    private String id;

    private HueDeviceEventType type;
    private Map<String, Object> update;

    public HueStatusEventDetails() { }

    public HueStatusEventDetails(String id, HueDeviceEventType type, Map<String, Object> update) {
        this.id = id;
        this.type = type;
        this.update = update;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public HueDeviceEventType getType() {
        return type;
    }

    public void setType(HueDeviceEventType type) {
        this.type = type;
    }

    public Map<String, Object> getUpdate() {
        return update;
    }

    public void setUpdate(Map<String, Object> update) {
        this.update = update;
    }
}
