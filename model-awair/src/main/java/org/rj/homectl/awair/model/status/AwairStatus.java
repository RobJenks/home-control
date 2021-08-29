package org.rj.homectl.awair.model.status;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AwairStatus {
    private OffsetDateTime timestamp;
    private Integer score;
    @JsonAlias({ "dew_point" })
    private Float dewPoint;
    private Float temp;
    @JsonAlias({ "humid" })
    private Float humidity;
    @JsonAlias({ "abs_humid" })
    private Float absoluteHumidity;
    @JsonAlias({ "co2" })
    private Integer co2;
    @JsonAlias({ "co2_est" })
    private Integer estimatedCo2;
    @JsonAlias({ "voc" })
    private Integer voc;
    @JsonAlias({ "voc_baseline" })
    private Long vocBaseline;
    @JsonAlias({ "voc_h2_raw" })
    private Integer vocRawH2;
    @JsonAlias({ "voc_ethanol_raw" })
    private Integer vocRawEthanol;
    @JsonAlias({ "pm25" })
    private Integer pm25;
    @JsonAlias({ "pm10_est" })
    private Integer estimatedPm10;

    public AwairStatus() { }

    @Override
    public String toString() {
        return String.format("Awair status (score: %d, timestamp: %s", getScore(), getTimestamp());
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(OffsetDateTime timestamp) {
        this.timestamp = timestamp;
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

    public Integer getCo2() {
        return co2;
    }

    public void setCo2(Integer co2) {
        this.co2 = co2;
    }

    public Integer getEstimatedCo2() {
        return estimatedCo2;
    }

    public void setEstimatedCo2(Integer estimatedCo2) {
        this.estimatedCo2 = estimatedCo2;
    }

    public Integer getVoc() {
        return voc;
    }

    public void setVoc(Integer voc) {
        this.voc = voc;
    }

    public Long getVocBaseline() {
        return vocBaseline;
    }

    public void setVocBaseline(Long vocBaseline) {
        this.vocBaseline = vocBaseline;
    }

    public Integer getVocRawH2() {
        return vocRawH2;
    }

    public void setVocRawH2(Integer vocRawH2) {
        this.vocRawH2 = vocRawH2;
    }

    public Integer getVocRawEthanol() {
        return vocRawEthanol;
    }

    public void setVocRawEthanol(Integer vocRawEthanol) {
        this.vocRawEthanol = vocRawEthanol;
    }

    public Integer getPm25() {
        return pm25;
    }

    public void setPm25(Integer pm25) {
        this.pm25 = pm25;
    }

    public Integer getEstimatedPm10() {
        return estimatedPm10;
    }

    public void setEstimatedPm10(Integer estimatedPm10) {
        this.estimatedPm10 = estimatedPm10;
    }
}
