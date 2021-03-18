package org.rj.homectl.status.hue;

import org.rj.homectl.hue.model.Light;
import org.rj.homectl.status.events.StatusEventContent;

import java.util.HashMap;
import java.util.Map;

public class HueStatusData extends HashMap<String, Light>
                          implements StatusEventContent {

    public HueStatusData() {
        super();
    }

    public HueStatusData(Map<String, Light> data) {
        if (data != null) {
            this.putAll(data);
        }
    }

}
