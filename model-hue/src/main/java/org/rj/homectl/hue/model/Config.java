package org.rj.homectl.hue.model;

import java.util.Map;

public class Config {
    private String archetype;
    private Map<String, Object> startup;
    private String direction;
    private String function;

    public Config() { }

    public String getArchetype() {
        return archetype;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    public Map<String, Object> getStartup() {
        return startup;
    }

    public void setStartup(Map<String, Object> startup) {
        this.startup = startup;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
