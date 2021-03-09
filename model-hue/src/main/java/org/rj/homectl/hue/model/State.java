package org.rj.homectl.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class State {
    @JsonProperty("sat")
    private Integer saturation;
    private Boolean on;
    private List<Float> xy;
    private Integer hue;
    @JsonProperty("bri")
    private Integer brightness;
    private String mode;
    private Integer ct;
    private String alert;
    @JsonProperty("colormode")
    private String colorMode;
    private String effect;
    private Boolean reachable;

    public State() { }

    public Integer getSaturation() {
        return saturation;
    }

    public void setSaturation(Integer saturation) {
        this.saturation = saturation;
    }

    public Boolean getOn() {
        return on;
    }

    public void setOn(Boolean on) {
        this.on = on;
    }

    public List<Float> getXy() {
        return xy;
    }

    public void setXy(List<Float> xy) {
        this.xy = xy;
    }

    public Integer getHue() {
        return hue;
    }

    public void setHue(Integer hue) {
        this.hue = hue;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public Integer getCt() {
        return ct;
    }

    public void setCt(Integer ct) {
        this.ct = ct;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getColorMode() {
        return colorMode;
    }

    public void setColorMode(String colorMode) {
        this.colorMode = colorMode;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public Boolean getReachable() {
        return reachable;
    }

    public void setReachable(Boolean reachable) {
        this.reachable = reachable;
    }
}
