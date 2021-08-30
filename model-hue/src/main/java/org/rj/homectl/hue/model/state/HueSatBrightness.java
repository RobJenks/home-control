package org.rj.homectl.hue.model.state;

public class HueSatBrightness {
    private Integer hue;
    private Integer saturation;
    private Integer brightness;

    public HueSatBrightness() { }

    public HueSatBrightness(Integer hue, Integer saturation, Integer brightness) {
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public Integer getHue() {
        return hue;
    }

    public void setHue(Integer hue) {
        this.hue = hue;
    }

    public Integer getSaturation() {
        return saturation;
    }

    public void setSaturation(Integer saturation) {
        this.saturation = saturation;
    }

    public Integer getBrightness() {
        return brightness;
    }

    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }
}
