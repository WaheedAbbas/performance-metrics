package com.metrics.performancemetrics.dto;

import jakarta.validation.constraints.NotNull;

import static com.metrics.performancemetrics.util.ResponseMessages.INVALID_METRIC_VALUE;

public record NewMetricValue(@NotNull(message = INVALID_METRIC_VALUE) Double value) {
}
