package org.rj.homectl.common.model;

import org.rj.homectl.common.model.util.CoordSpace;
import org.rj.homectl.common.model.util.Vector2;

public class Schematic {
    private CoordSpace coordSpace;
    private Vector2 location;
    private Vector2 size;

    public Schematic() { }

    public CoordSpace getCoordSpace() {
        return coordSpace;
    }

    public void setCoordSpace(CoordSpace coordSpace) {
        this.coordSpace = coordSpace;
    }

    public Vector2 getLocation() {
        return location;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public Vector2 getSize() {
        return size;
    }

    public void setSize(Vector2 size) {
        this.size = size;
    }
}
