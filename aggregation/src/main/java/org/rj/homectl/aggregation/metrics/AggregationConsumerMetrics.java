package org.rj.homectl.aggregation.metrics;

import com.fasterxml.jackson.annotation.JsonAlias;

public class AggregationConsumerMetrics {
    @JsonAlias("records-consumed-total")
    private Double recordsConsumedTotal;
    @JsonAlias("bytes-consumed-total")
    private Double bytesConsumedTotal;
    @JsonAlias("records-lag-max")
    private Double recordsLagMax;
    @JsonAlias("fetch-rate")
    private Double fetchRate;
    @JsonAlias("fetch-total")
    private Double fetchTotal;
    @JsonAlias("assigned-partitions")
    private Double assignedPartitions;
    @JsonAlias("fetch-latency-avg")
    private Double fetchLatencyAvg;
    @JsonAlias("fetch-latency-max")
    private Double fetchLatencyMax;
    @JsonAlias("network-io-rate")
    private Double networkIoRate;
    @JsonAlias("fetch-size-avg")
    private Double fetchSizeAvg;
    @JsonAlias("fetch-size-max")
    private Double fetchSizeMax;
    @JsonAlias("records-per-request-avg")
    private Double recordsPerRequestAvg;
    @JsonAlias("records-per-request-max")
    private Double recordsPerRequestMax;
    @JsonAlias("last-heartbeat-seconds-ago")
    private Double lastHeartbeatSecondsAgo;
    @JsonAlias("last-rebalance-seconds-ago")
    private Double lastRebalanceSecondsAgo;
    @JsonAlias("rebalance-total")
    private Double rebalanceTotal;

    public AggregationConsumerMetrics() { }

    public Double getRecordsConsumedTotal() {
        return recordsConsumedTotal;
    }

    public void setRecordsConsumedTotal(Double recordsConsumedTotal) {
        this.recordsConsumedTotal = recordsConsumedTotal;
    }

    public Double getBytesConsumedTotal() {
        return bytesConsumedTotal;
    }

    public void setBytesConsumedTotal(Double bytesConsumedTotal) {
        this.bytesConsumedTotal = bytesConsumedTotal;
    }

    public Double getRecordsLagMax() {
        return recordsLagMax;
    }

    public void setRecordsLagMax(Double recordsLagMax) {
        this.recordsLagMax = recordsLagMax;
    }

    public Double getFetchRate() {
        return fetchRate;
    }

    public void setFetchRate(Double fetchRate) {
        this.fetchRate = fetchRate;
    }

    public Double getFetchTotal() {
        return fetchTotal;
    }

    public void setFetchTotal(Double fetchTotal) {
        this.fetchTotal = fetchTotal;
    }

    public Double getAssignedPartitions() {
        return assignedPartitions;
    }

    public void setAssignedPartitions(Double assignedPartitions) {
        this.assignedPartitions = assignedPartitions;
    }

    public Double getFetchLatencyAvg() {
        return fetchLatencyAvg;
    }

    public void setFetchLatencyAvg(Double fetchLatencyAvg) {
        this.fetchLatencyAvg = fetchLatencyAvg;
    }

    public Double getFetchLatencyMax() {
        return fetchLatencyMax;
    }

    public void setFetchLatencyMax(Double fetchLatencyMax) {
        this.fetchLatencyMax = fetchLatencyMax;
    }

    public Double getNetworkIoRate() {
        return networkIoRate;
    }

    public void setNetworkIoRate(Double networkIoRate) {
        this.networkIoRate = networkIoRate;
    }

    public Double getFetchSizeAvg() {
        return fetchSizeAvg;
    }

    public void setFetchSizeAvg(Double fetchSizeAvg) {
        this.fetchSizeAvg = fetchSizeAvg;
    }

    public Double getFetchSizeMax() {
        return fetchSizeMax;
    }

    public void setFetchSizeMax(Double fetchSizeMax) {
        this.fetchSizeMax = fetchSizeMax;
    }

    public Double getRecordsPerRequestAvg() {
        return recordsPerRequestAvg;
    }

    public void setRecordsPerRequestAvg(Double recordsPerRequestAvg) {
        this.recordsPerRequestAvg = recordsPerRequestAvg;
    }

    public Double getRecordsPerRequestMax() {
        return recordsPerRequestMax;
    }

    public void setRecordsPerRequestMax(Double recordsPerRequestMax) {
        this.recordsPerRequestMax = recordsPerRequestMax;
    }

    public Double getLastHeartbeatSecondsAgo() {
        return lastHeartbeatSecondsAgo;
    }

    public void setLastHeartbeatSecondsAgo(Double lastHeartbeatSecondsAgo) {
        this.lastHeartbeatSecondsAgo = lastHeartbeatSecondsAgo;
    }

    public Double getLastRebalanceSecondsAgo() {
        return lastRebalanceSecondsAgo;
    }

    public void setLastRebalanceSecondsAgo(Double lastRebalanceSecondsAgo) {
        this.lastRebalanceSecondsAgo = lastRebalanceSecondsAgo;
    }

    public Double getRebalanceTotal() {
        return rebalanceTotal;
    }

    public void setRebalanceTotal(Double rebalanceTotal) {
        this.rebalanceTotal = rebalanceTotal;
    }
}
