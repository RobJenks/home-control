package org.rj.homectl.hue.model.status;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Config config = (Config) o;

        return new EqualsBuilder().append(archetype, config.archetype).append(startup, config.startup)
                .append(direction, config.direction).append(function, config.function).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(archetype).append(startup)
                .append(direction).append(function).toHashCode();
    }
}
