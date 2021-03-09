package org.rj.homectl.hue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Light {
    private String name;
    private String type;
    @JsonProperty("modelid")
    private String modelId;
    @JsonProperty("manufacturername")
    private String manufacturerName;
    @JsonProperty("uniqueid")
    private String uniqueId;
    @JsonProperty("productid")
    private String productId;
    private State state;
    private Capabilities capabilities;
    private Config config;
    @JsonProperty("swupdate")
    private SWUpdate swUpdate;
    @JsonProperty("swversion")
    private String swVersion;
    @JsonProperty("swconfigid")
    private String swConfigId;

    public Light() { }

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
}
