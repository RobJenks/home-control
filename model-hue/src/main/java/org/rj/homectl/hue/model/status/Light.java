package org.rj.homectl.hue.model.status;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Light {
    private String name;
    private String type;
    @JsonAlias({ "modelid" })
    private String modelId;
    @JsonAlias({ "manufacturername" })
    private String manufacturerName;
    @JsonAlias({ "uniqueid" })
    private String uniqueId;
    @JsonAlias({ "productid" })
    private String productId;
    private State state;
    private Capabilities capabilities;
    private Config config;
    @JsonAlias({ "swupdate" })
    private SWUpdate swUpdate;
    @JsonAlias({ "swversion" })
    private String swVersion;
    @JsonAlias({ "swconfigid" })
    private String swConfigId;

    public Light() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Capabilities getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(Capabilities capabilities) {
        this.capabilities = capabilities;
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public SWUpdate getSwUpdate() {
        return swUpdate;
    }

    public void setSwUpdate(SWUpdate swUpdate) {
        this.swUpdate = swUpdate;
    }

    public String getSwVersion() {
        return swVersion;
    }

    public void setSwVersion(String swVersion) {
        this.swVersion = swVersion;
    }

    public String getSwConfigId() {
        return swConfigId;
    }

    public void setSwConfigId(String swConfigId) {
        this.swConfigId = swConfigId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Light light = (Light) o;

        return new EqualsBuilder()
                .append(name, light.name).append(type, light.type).append(modelId, light.modelId)
                .append(manufacturerName, light.manufacturerName).append(uniqueId, light.uniqueId)
                .append(productId, light.productId).append(state, light.state).append(capabilities, light.capabilities)
                .append(config, light.config).append(swUpdate, light.swUpdate).append(swVersion, light.swVersion)
                .append(swConfigId, light.swConfigId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(name).append(type).append(modelId)
                .append(manufacturerName).append(uniqueId).append(productId).append(state).append(capabilities)
                .append(config).append(swUpdate).append(swVersion).append(swConfigId).toHashCode();
    }
}
