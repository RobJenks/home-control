package org.rj.homectl.st.model.state;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rj.homectl.common.model.DeviceState;
import org.rj.homectl.st.model.CapabilityReference;
import org.rj.homectl.st.model.ComponentStatus;
import org.rj.homectl.st.model.Device;
import org.rj.homectl.st.model.DeviceStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StDeviceState extends DeviceState {
    private String id;
    private String name;
    private StDeviceMetadata metadata;
    private List<StAdvertisedComponent> advertisedComponentCapabilities;
    private Map<String, ComponentStatus> components;

    public StDeviceState() { }

    public void updateFromListing(Device device) {
        this.id = device.getDeviceId();
        this.name = device.getLabel();

        if (this.metadata == null) this.metadata = new StDeviceMetadata();
        this.metadata.setTypeId(device.getDeviceTypeId());
        this.metadata.setTypeName(device.getDeviceTypeName());
        this.metadata.setTypeShortName(device.getName());
        this.metadata.setNetworkType(device.getDeviceNetworkType());
        this.metadata.setManufacturer(device.getDeviceManufacturerCode());

        this.advertisedComponentCapabilities = Optional.ofNullable(device.getComponents()).orElseGet(List::of).stream()
                .map(x -> new StAdvertisedComponent(x.getId(), Optional.ofNullable(x.getCapabilities()).orElseGet(List::of).stream()
                        .map(CapabilityReference::getId)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

        this.recordUpdate();
    }

    public void updateFromStatus(DeviceStatus status) {
        if (status.getComponents() != null) {
            this.components = status.getComponents();
            this.recordUpdate();
        }
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object getFullState() {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StDeviceMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(StDeviceMetadata metadata) {
        this.metadata = metadata;
    }

    public List<StAdvertisedComponent> getAdvertisedComponentCapabilities() {
        return advertisedComponentCapabilities;
    }

    public void setAdvertisedComponentCapabilities(List<StAdvertisedComponent> advertisedComponentCapabilities) {
        this.advertisedComponentCapabilities = advertisedComponentCapabilities;
    }

    public Map<String, ComponentStatus> getComponents() {
        return components;
    }

    public void setComponents(Map<String, ComponentStatus> components) {
        this.components = components;
    }
}
