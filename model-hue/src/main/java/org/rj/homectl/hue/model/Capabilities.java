package org.rj.homectl.hue.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Map;

public class Capabilities {
    private Map<String, Object> streaming;
    private boolean certified;
    private Map<String, Object> control;

    public Capabilities() { }

    public Map<String, Object> getStreaming() {
        return streaming;
    }

    public void setStreaming(Map<String, Object> streaming) {
        this.streaming = streaming;
    }

    public boolean isCertified() {
        return certified;
    }

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public Map<String, Object> getControl() {
        return control;
    }

    public void setControl(Map<String, Object> control) {
        this.control = control;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Capabilities that = (Capabilities) o;

        return new EqualsBuilder().append(certified, that.certified).append(streaming, that.streaming)
                .append(control, that.control).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(streaming).append(certified)
                .append(control).toHashCode();
    }
}
