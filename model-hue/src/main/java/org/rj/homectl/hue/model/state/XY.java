package org.rj.homectl.hue.model.state;

import java.util.List;

public class XY {
    private Float x;
    private Float y;

    public XY() { }

    public XY(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    public static XY fromStatusData(List<Float> data) {
        if (data == null || data.size() < 2) return new XY();
        return new XY(data.get(0), data.get(1));
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }
}
