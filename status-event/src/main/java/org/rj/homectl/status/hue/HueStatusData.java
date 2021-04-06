package org.rj.homectl.status.hue;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rj.homectl.status.events.StatusEventContent;


public class HueStatusData implements StatusEventContent {

    private HueEventType type;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HueStatusLightData lights;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HueStatusEventDetails event;

    public HueStatusData() { }

    public HueStatusData(HueEventType type, HueStatusLightData lights, HueStatusEventDetails event) {
        this.type = type;
        this.lights = lights;
        this.event = event;
    }

    public static HueStatusData snapshot(HueStatusLightData lights) {
        return new HueStatusData(HueEventType.Snapshot, lights, null);
    }

    public static HueStatusData updates(HueStatusLightData lights) {
        return new HueStatusData(HueEventType.Update, lights, null);
    }

    public static HueStatusData event(HueStatusEventDetails event) {
        return new HueStatusData(HueEventType.Event, null, event);
    }

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

    public HueStatusEventDetails getEvent() {
        return event;
    }

    public void setEvent(HueStatusEventDetails event) {
        this.event = event;
    }
}
