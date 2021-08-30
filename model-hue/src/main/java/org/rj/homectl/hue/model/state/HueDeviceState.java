package org.rj.homectl.hue.model.state;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rj.homectl.common.model.DeviceState;
import org.rj.homectl.hue.model.status.Config;
import org.rj.homectl.hue.model.status.Light;

import java.util.Optional;

public class HueDeviceState extends DeviceState {
    private String name;
    private String hueId;
    private String model;
    private String lightType;
    private String lightArchetype;
    private boolean reachable;
    private boolean on;
    private HueLightColorMode mode;
    private XY xy;
    private HueSatBrightness hsb;
    private Integer colorTemp;

    public HueDeviceState() { }

    public void updateFromStatus(String hueId, Light status) {
        final var state = status.getState();
        if (state == null) return;

        this.name = status.getName();
        this.hueId = hueId;
        this.model = status.getModelId();
        this.lightType = status.getType();
        this.lightArchetype = Optional.ofNullable(status.getConfig()).map(Config::getArchetype).orElse("unknown");
        this.reachable = state.getReachable();
        this.on = state.getOn();
        this.mode = HueLightColorMode.forKey(state.getColorMode());
        this.xy = XY.fromStatusData(state.getXy());
        this.hsb = new HueSatBrightness(state.getHue(), state.getSaturation(), state.getBrightness());
        this.colorTemp = state.getCt();

        this.recordUpdate();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHueId() {
        return hueId;
    }

    public void setHueId(String hueId) {
        this.hueId = hueId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getLightType() {
        return lightType;
    }

    public void setLightType(String lightType) {
        this.lightType = lightType;
    }

    public String getLightArchetype() {
        return lightArchetype;
    }

    public void setLightArchetype(String lightArchetype) {
        this.lightArchetype = lightArchetype;
    }

    public boolean isReachable() {
        return reachable;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public HueLightColorMode getMode() {
        return mode;
    }

    public void setMode(HueLightColorMode mode) {
        this.mode = mode;
    }

    public XY getXy() {
        return xy;
    }

    public void setXy(XY xy) {
        this.xy = xy;
    }

    public HueSatBrightness getHsb() {
        return hsb;
    }

    public void setHsb(HueSatBrightness hsb) {
        this.hsb = hsb;
    }

    public Integer getColorTemp() {
        return colorTemp;
    }

    public void setColorTemp(Integer colorTemp) {
        this.colorTemp = colorTemp;
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object getFullState() {
        return null;
    }
}
