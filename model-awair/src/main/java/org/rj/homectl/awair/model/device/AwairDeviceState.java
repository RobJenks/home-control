package org.rj.homectl.awair.model.device;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.rj.homectl.awair.model.status.AwairStatus;
import org.rj.homectl.common.model.DeviceState;

public class AwairDeviceState extends DeviceState {
    private Integer score;
    private Float dewPoint;
    private Float temp;
    private Float humidity;
    private Float absoluteHumidity;
    private Integer co2;
    private Integer estimatedCo2;
    private Integer voc;
    private Long vocBaseline;
    private Integer vocRawH2;
    private Integer vocRawEthanol;
    private Integer pm25;
    private Integer estimatedPm10;

    public AwairDeviceState() { }

    public void updateFromStatus(AwairStatus status) {
        this.score = status.getScore();
        this.dewPoint = status.getDewPoint();
        this.temp = status.getTemp();
        this.humidity = status.getHumidity();
        this.absoluteHumidity = status.getAbsoluteHumidity();
        this.co2 = status.getCo2();
        this.estimatedCo2 = status.getEstimatedCo2();
        this.voc = status.getVoc();
        this.vocBaseline = status.getVocBaseline();
        this.vocRawH2 = status.getVocRawH2();
        this.vocRawEthanol = status.getVocRawEthanol();
        this.pm25 = status.getPm25();
        this.estimatedPm10 = status.getEstimatedPm10();

        setLastUpdate(status.getTimestamp());
    }

    @Override
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object getFullState() {
        // No additional or detailed state; Awair status response is sufficient to also
        // be used as the complete device state
        return null;
    }

    public Integer getScore() {
        return score;
    }

    public Float getDewPoint() {
        return dewPoint;
    }

    public Float getTemp() {
        return temp;
    }

    public Float getHumidity() {
        return humidity;
    }

    public Float getAbsoluteHumidity() {
        return absoluteHumidity;
    }

    public Integer getCo2() {
        return co2;
    }

    public Integer getEstimatedCo2() {
        return estimatedCo2;
    }

    public Integer getVoc() {
        return voc;
    }

    public Long getVocBaseline() {
        return vocBaseline;
    }

    public Integer getVocRawH2() {
        return vocRawH2;
    }

    public Integer getVocRawEthanol() {
        return vocRawEthanol;
    }

    public Integer getPm25() {
        return pm25;
    }

    public Integer getEstimatedPm10() {
        return estimatedPm10;
    }
}
