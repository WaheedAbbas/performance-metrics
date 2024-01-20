package com.metrics.performancemetrics.dto;

import jakarta.validation.constraints.NotEmpty;
import static com.metrics.performancemetrics.util.ResponseMessages.INVALID_METRIC_NAME;

public record NewMetric(@NotEmpty(message = INVALID_METRIC_NAME) String name){}
