package org.rj.homectl.status.hue;

import org.rj.homectl.hue.model.status.Light;

import java.util.HashMap;
import java.util.Map;

public class HueStatusLightData extends HashMap<String, Light> {
    public HueStatusLightData() {
        super();
    }

    public HueStatusLightData(Map<? extends String, ? extends Light> m) {
        super(m);
    }
}
