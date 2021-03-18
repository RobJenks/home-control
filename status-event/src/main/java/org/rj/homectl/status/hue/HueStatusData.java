package org.rj.homectl.status.hue;

import org.rj.homectl.status.events.StatusEventContent;


public class HueStatusData implements StatusEventContent {

    private HueEventType type;
    private HueStatusLightData lights;

    public HueStatusData() { }

    public HueStatusData(HueEventType type, HueStatusLightData lights) {
        this.type = type;
        this.lights = lights;
    }

    public HueEventType getType() {
        return type;
    }

    public void setType(HueEventType type) {
        this.type = type;
    }

    public HueStatusLightData getLights() {
        return lights;
    }

    public void setLights(HueStatusLightData lights) {
        this.lights = lights;
    }
}
