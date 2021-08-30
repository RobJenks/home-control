package org.rj.homectl.hue.model.status;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;

public class State {
    @JsonAlias({ "sat" })
    private Integer saturation;
    private Boolean on;
    private List<Float> xy;
    private Integer hue;
    @JsonAlias({ "bri" })
    private Integer brightness;
    private String mode;
    private Integer ct;
    private String alert;
    @JsonAlias({ "colormode" })
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        return new EqualsBuilder().append(saturation, state.saturation).append(on, state.on).append(xy, state.xy)
                .append(hue, state.hue).append(brightness, state.brightness).append(mode, state.mode).append(ct, state.ct)
                .append(alert, state.alert).append(colorMode, state.colorMode).append(effect, state.effect)
                .append(reachable, state.reachable).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(saturation).append(on).append(xy)
                .append(hue).append(brightness).append(mode).append(ct).append(alert).append(colorMode).append(effect)
                .append(reachable).toHashCode();
    }
}
