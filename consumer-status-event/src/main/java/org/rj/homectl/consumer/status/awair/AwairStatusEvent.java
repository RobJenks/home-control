package org.rj.homectl.consumer.status.awair;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.rj.homectl.common.beans.events.StatusEvent;

public class AwairStatusEvent extends StatusEvent {
    private Integer score;
    @JsonProperty("dew_point")
    private Float dewPoint;
    private Float temp;
    @JsonProperty("humid")
    private Float humidity;
    @JsonProperty("abs_humid")
    private Float absoluteHumidity;
    @JsonProperty("co2")
    private Integer CO2;
    @JsonProperty("co2_est")
    private Integer estimatedCO2;
    @JsonProperty("voc")
    private Integer VOC;
    @JsonProperty("voc_baseline")
    private Long VOCBaseline;
    @JsonProperty("voc_h2_raw")
    private Integer VOCRawH2;
    @JsonProperty("voc_ethanol_raw")
    private Integer VOCRawEthanol;
    @JsonProperty("pm25")
    private Integer PM25;
    @JsonProperty("pm10_est")
    private Integer estimatedPM10;

    public AwairStatusEvent() { }

    @Override
    public String toString() {
        return "Status event [type: Awair, timestamp: " + getTimestamp() + ", score: " + getScore() + "]";
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Float getDewPoint() {
        return dewPoint;
    }

    public void setDewPoint(Float dewPoint) {
        this.dewPoint = dewPoint;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Float getAbsoluteHumidity() {
        return absoluteHumidity;
    }

    public void setAbsoluteHumidity(Float absoluteHumidity) {
        this.absoluteHumidity = absoluteHumidity;
    }

    public Integer getCO2() {
        return CO2;
    }

    public void setCO2(Integer CO2) {
        this.CO2 = CO2;
    }

    public Integer getEstimatedCO2() {
        return estimatedCO2;
    }

    public void setEstimatedCO2(Integer estimatedCO2) {
        this.estimatedCO2 = estimatedCO2;
    }

    public Integer getVOC() {
        return VOC;
    }

    public void setVOC(Integer VOC) {
        this.VOC = VOC;
    }

    public Long getVOCBaseline() {
        return VOCBaseline;
    }

    public void setVOCBaseline(Long VOCBaseline) {
        this.VOCBaseline = VOCBaseline;
    }

    public Integer getVOCRawH2() {
        return VOCRawH2;
    }

    public void setVOCRawH2(Integer VOCRawH2) {
        this.VOCRawH2 = VOCRawH2;
    }

    public Integer getVOCRawEthanol() {
        return VOCRawEthanol;
    }

    public void setVOCRawEthanol(Integer VOCRawEthanol) {
        this.VOCRawEthanol = VOCRawEthanol;
    }

    public Integer getPM25() {
        return PM25;
    }

    public void setPM25(Integer PM25) {
        this.PM25 = PM25;
    }

    public Integer getEstimatedPM10() {
        return estimatedPM10;
    }

    public void setEstimatedPM10(Integer estimatedPM10) {
        this.estimatedPM10 = estimatedPM10;
    }
}
