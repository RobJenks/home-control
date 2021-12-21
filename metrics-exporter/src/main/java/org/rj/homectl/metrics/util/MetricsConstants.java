package org.rj.homectl.metrics.util;

public abstract class MetricsConstants {
    public static final String NAMESPACE = "home_control";
    
    public static final String METRIC_ENV_SCORE = "score";
    public static final String METRIC_ENV_TEMP = "temperature";
    public static final String METRIC_ENV_HUMIDITY = "humidity";
    public static final String METRIC_ENV_ABS_HUMIDITY = "abs_humidity";
    public static final String METRIC_ENV_DEW_POINT = "dew_point";
    public static final String METRIC_ENV_CO2 = "co2";
    public static final String METRIC_ENV_EST_CO2 = "estimated_co2";
    public static final String METRIC_ENV_VOC = "voc";
    public static final String METRIC_ENV_VOC_BASELINE = "voc_baseline";
    public static final String METRIC_ENV_VOC_RAW_H2 = "voc_raw_h2";
    public static final String METRIC_ENV_VOC_RAW_ETHANOL = "voc_raw_ethanol";
    public static final String METRIC_ENV_PM25 = "pm25";
    public static final String METRIC_ENV_EST_PM10 = "est_pm10";

    public static final String METRIC_HUE_DEVICES_ON = "hue_devices_on";
}
