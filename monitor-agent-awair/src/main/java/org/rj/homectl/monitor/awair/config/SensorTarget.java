package org.rj.homectl.monitor.awair.config;

public class SensorTarget {
    private final String id;
    private final String target;

    public SensorTarget(String id, String target) {
        this.id = id;
        this.target = target;
    }

    public String getId() {
        return id;
    }

    public String getTarget() {
        return target;
    }
}
