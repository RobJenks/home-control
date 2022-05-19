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

    public static final String METRIC_CLUSTER_CONSUMER_RECORDS_CONSUMED = "cluster_metrics_consumer_records_consumed_total";
    public static final String METRIC_CLUSTER_CONSUMER_BYTES_CONSUMED = "cluster_metrics_consumer_bytes_consumed_total";
    public static final String METRIC_CLUSTER_CONSUMER_RECORD_LAG_MAX = "cluster_metrics_consumer_record_lag_max";
    public static final String METRIC_CLUSTER_CONSUMER_FETCH_RATE = "cluster_metrics_consumer_fetch_rate";
    public static final String METRIC_CLUSTER_CONSUMER_FETCH_TOTAL = "cluster_metrics_consumer_fetch_total";
    public static final String METRIC_CLUSTER_CONSUMER_FETCH_LATENCY = "cluster_metrics_consumer_fetch_latency";
    public static final String METRIC_CLUSTER_CONSUMER_FETCH_SIZE = "cluster_metrics_consumer_network_fetch_size";
    public static final String METRIC_CLUSTER_CONSUMER_NETWORK_IO_RATE = "cluster_metrics_consumer_network_io_rate";
    public static final String METRIC_CLUSTER_CONSUMER_LAST_HEARTBEAT_SECS = "cluster_metrics_consumer_last_heartbeat_secs";
}
