package org.rj.homectl.hue.model;

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
}
