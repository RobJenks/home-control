package org.rj.homectl.status.hue;

import com.google.common.collect.MapDifference;
import org.rj.homectl.common.util.DiffMap;

public class HueStatusEventDetails {
    private String id;

    private HueDeviceEventType type;
    private DiffMap update;

    public HueStatusEventDetails() { }

    public HueStatusEventDetails(String id, HueDeviceEventType type, DiffMap update) {
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

    public DiffMap getUpdate() {
        return update;
    }

    public void setUpdate(DiffMap update) {
        this.update = update;
    }
}
