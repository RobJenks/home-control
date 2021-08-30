package org.rj.homectl.st.model.state;

import java.util.List;

public class StAdvertisedComponent {
    private String id;
    private List<String> capabilities;

    public StAdvertisedComponent() { }

    public StAdvertisedComponent(String id, List<String> capabilities) {
        this.id = id;
        this.capabilities = capabilities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<String> capabilities) {
        this.capabilities = capabilities;
    }
}
