package org.rj.homectl.hue.model.state;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public enum HueLightColorMode {
    Unknown(""),
    XYCoords("xy"),
    HueSatBrightness("hsb"),
    ColorTemp("ct");

    private static Logger LOG = LoggerFactory.getLogger(HueLightColorMode.class);
    private final String hueKey;

    HueLightColorMode(final String hueKey) {
        this.hueKey = hueKey;
    }

    public static HueLightColorMode forKey(String key) {
        return Arrays.stream(HueLightColorMode.values())
                .filter(x -> x.hueKey.equals(key))
                .findAny()
                .orElseGet(() -> {
                    LOG.warn("Cannot parse unknown Hue color mode key '{}'", key);
                    return HueLightColorMode.Unknown;
                });
    }
}
