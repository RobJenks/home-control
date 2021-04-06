package org.rj.homectl.status.hue;

import com.google.common.collect.MapDifference;

public class HueStatusEventDetails {
    private String id;

    private HueDeviceEventType type;
    private MapDifference<String, Object> update;

    public HueStatusEventDetails() { }

    public HueStatusEventDetails(String id, HueDeviceEventType type, MapDifference<String, Object> update) {
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

    public MapDifference<String, Object> getUpdate() {
        return update;
    }

    public void setUpdate(MapDifference<String, Object> update) {
        this.update = update;
    }
}
