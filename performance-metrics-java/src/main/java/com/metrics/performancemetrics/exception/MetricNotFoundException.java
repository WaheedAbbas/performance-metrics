package com.metrics.performancemetrics.exception;

import com.metrics.performancemetrics.util.ResponseMessages;

public class MetricNotFoundException extends RuntimeException {

    public MetricNotFoundException(Integer metricId)
    {
        super(ResponseMessages.INVALID_METRIC_ID+ " "+metricId);
    }
}
